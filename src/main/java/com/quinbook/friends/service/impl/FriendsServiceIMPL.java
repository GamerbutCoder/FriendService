package com.quinbook.friends.service.impl;

import com.mongodb.client.MongoClients;
import com.quinbook.friends.client.NotificationClient;
import com.quinbook.friends.client.SessionClient;
import com.quinbook.friends.client.UserClient;
import com.quinbook.friends.dto.*;
import com.quinbook.friends.entity.Friend;
import com.quinbook.friends.entity.Friends;
import com.quinbook.friends.entity.Login;
import com.quinbook.friends.entity.Policy;
import com.quinbook.friends.repository.FriendsRepository;
import com.quinbook.friends.repository.LoginDao;
import com.quinbook.friends.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class FriendsServiceIMPL implements FriendsService {
    static MongoOperations mongoOperations = new MongoTemplate(MongoClients.create(),"quinbook");

    @Autowired
    private SessionClient sessionClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private NotificationClient notificationClient;

    @Autowired
    private FriendsRepository friendsRepository;

    @Autowired
    private LoginDao loginDao;


    @Override
    @Transactional
    public boolean addFriends(FriendsRequestDTO requestDTO,String sessionId) {
        if(sessionValidate(sessionId) ==null){
            return false;
        }
        String userName = requestDTO.getUserName();
        String friendUserName = requestDTO.getFriendUserName();
        if(userName == null || friendUserName == null || userName.length()==0 || friendUserName.length()==0 || requestDTO.getSelfDetails() ==null){
            return false;
        }
        else{
            boolean checker = true;
            checker&=addFriendsInDB(userName,friendUserName);
            checker&=addFriendsInDB(friendUserName,userName);
            if(checker){
                FriendRequestAcceptanceNotification obj  = new FriendRequestAcceptanceNotification();
                obj.setWhose(friendUserName);
                obj.setAcceptedBy(requestDTO.getSelfDetails());
                obj.setEventType("FRNDREQACC");
                notificationClient.sendAcceptanceNotification(obj);
                return true;
            }
            return false;
        }
    }


    @Override
    public boolean blockFriends(FriendsRequestDTO requestDTO,String sessionId) {
        if(sessionValidate(sessionId)==null){
            return false;
        }
        String userName = requestDTO.getUserName();
        String friendUserName = requestDTO.getFriendUserName();
        if(userName == null || friendUserName == null || userName.length()==0 || friendUserName.length()==0){
            return false;
        }
        else{

            Optional<Friends> optional = friendsRepository.findById(friendUserName);
            if(optional.isPresent()){
                Friends friends = friendsRepository.checkUserExistsInBlockList(friendUserName,userName); // checking if user has blocked friendUser
                if (friends == null) {
                    Friends obj = optional.get();
                    obj.getGotBlockedByList().add(userName);
                    friendsRepository.save(obj);
                }
            }
            else{
                Friends friends = new Friends();
                friends.setUserName(friendUserName);
                List<String> blockList = new ArrayList<>();
                blockList.add(userName);
                friends.setGotBlockedByList(blockList);
                List<Friend> friendList = new ArrayList<>();
                friends.setFriendList(friendList);
                Policy policy = new Policy();
                policy.setProfilePic("PUBLIC");
                policy.setFeed("PUBLIC");
                policy.setFriendList("PUBLIC");
                friends.setPolicy(policy);
                friendsRepository.save(friends);

            }
            boolean checker = true;
            checker&=removeFriendsFromDB(userName,friendUserName);
            checker&=removeFriendsFromDB(friendUserName,userName);

            return checker;
        }
    }

    @Override
    public boolean removeFriends(FriendsRequestDTO requestDTO,String sessionId) {
        if(sessionValidate(sessionId)==null){
            return false;
        }
        String userName = requestDTO.getUserName();
        String friendUserName = requestDTO.getFriendUserName();
        if(userName == null || friendUserName == null || userName.length()==0 || friendUserName.length()==0){
            return false;
        }
        else{
            boolean checker = true;
            checker&=removeFriendsFromDB(userName,friendUserName);
            checker&=removeFriendsFromDB(friendUserName,userName);
            return checker;
        }

    }

    @Transactional
    private boolean addFriendsInDB(String userName, String friendUserName){
        Optional<Friends> optional = friendsRepository.findById(userName);
        if(optional.isPresent()){
            List<Friends> f = mongoOperations.find(new Query(where("friendList.userName").is(friendUserName)),Friends.class);
            if (f == null || f.size() <= 0) {
                Friend friend = new Friend();
                FriendProfileDTO friendProfileDTO = userClient.getFriendProfile(friendUserName);
                friend.setProfilePic(friendProfileDTO.getImg());
                friend.setUserName(friendUserName);
                friend.setFullName(friendProfileDTO.getFullName());
                Friends friends = optional.get();
                friends.getFriendList().add(friend);
                friendsRepository.save(friends);
                return true;
            }
            return false;
        }
        else{
            Friends friends = new Friends();
            friends.setUserName(userName);
            List<Friend> friendList = new ArrayList<>();
            Friend friend = new Friend();
            friend.setUserName(friendUserName);
            FriendProfileDTO friendProfileDTO = userClient.getFriendProfile(friendUserName);
            friend.setFullName(friendProfileDTO.getFullName());
            friend.setProfilePic(friendProfileDTO.getImg());
            friendList.add(friend);
            friends.setFriendList(friendList);
            List<String> blockedList = new ArrayList<>();
            friends.setGotBlockedByList(blockedList);
            Policy policy = new Policy();
            policy.setProfilePic("PUBLIC");
            policy.setFeed("PUBLIC");
            policy.setFriendList("PUBLIC");
            friends.setPolicy(policy);
            friendsRepository.save(friends);
            return true;
        }
    }

    @Transactional
    private boolean removeFriendsFromDB(String userName,String friendUserName){
        Optional<Friends> optional = friendsRepository.findById(userName);
        if(optional.isPresent()){
            try{
                Friends friends = optional.get();
                Friend friend = new Friend();
                friend.setUserName(friendUserName);
                FriendProfileDTO friendProfileDTO = userClient.getFriendProfile(friendUserName);
                friend.setFullName(friendProfileDTO.getFullName());
                friend.setProfilePic(friendProfileDTO.getImg());
                //friends.getFriendList().remove(friend);
                Query query = new Query();
                Criteria criteria = Criteria.where("friendList.userName").is(friendUserName);
                query.addCriteria(criteria);
                Update update = new Update().pull("friendList",friend);
//                List<Friend> l = friends.getFriendList();
//                l.remove(friend);
//                friends.setFriendList(l);
//                friendsRepository.save(friends);

                mongoOperations.updateFirst(query,update,Friends.class);
                return true;

            }
            catch (Exception e){
                return false;
            }
        }
        return true;
    }

    @Override
    public FriendsSocialDTO fetchUserSocial(String userName) {
        Optional<Friends> friends = friendsRepository.findById(userName);
        if(friends.isPresent()){
            Friends response  = friends.get();
            FriendsSocialDTO friendsSocialDTO = new FriendsSocialDTO();
            friendsSocialDTO.setUserName(response.getUserName());
            friendsSocialDTO.setFriendList(response.getFriendList());
            Policy policy = new Policy();
            policy.setFeed(response.getPolicy().getFeed());
            policy.setFriendList(response.getPolicy().getFriendList());
            policy.setProfilePic(response.getPolicy().getProfilePic());
            friendsSocialDTO.setPolicy(policy);
            friendsSocialDTO.setGotBlockedByList(response.getGotBlockedByList());
            return friendsSocialDTO;
        }
        return null;
    }

    private String sessionValidate(String sessionId){

        List<Login> loginList= loginDao.findAll();
        System.out.println(loginList);
        HashMap<String,String> userMap=new HashMap<>();
        for(Login login:loginList)
        {
            String session=login.getSessionId();
            System.out.println(session);
            String userName=login.getUserName();
            System.out.println(userName);
            userMap.put(session,userName);
        }
        if (userMap.containsKey(sessionId)){
            return userMap.get(sessionId);
        }
        else{
            return null;
        }
    }

    @Override
    public ResponseEntity<List<Friend>> fetchFriendList(String sessionId) {
        ResponseEntity<List<Friend>> response;

        //String userName = sessionClient.getUserName(sessionId);
        String userName = sessionValidate(sessionId);
        if( userName == null || userName.length() == 0){
            response = new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
            return response;
        }
        else{
            Optional<Friends> optional = friendsRepository.findById(userName);
            if(optional.isPresent()){
                List<Friend> friendList = optional.get().getFriendList();
                response = new ResponseEntity<>(friendList,HttpStatus.OK);
                return response;
            }
            else{
                response = new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
                return response;
            }
        }


    }

    @Override
    public boolean unblockUser(FriendsRequestDTO requestDTO, String sessionId) {
        if(requestDTO == null || sessionId == null || sessionId.length()==0) return false;
        String userName = requestDTO.getUserName();
        String friendUserName = requestDTO.getFriendUserName();
        if(userName.length()==0 || friendUserName.length()==0) return false;
        else{
            Optional<Friends> optional = friendsRepository.findById(friendUserName);
            if(optional.isPresent()){
                Friends friends = optional.get();
                if(friends.getGotBlockedByList().contains(userName)){
                    List<String> l = friends.getGotBlockedByList();
                    l.remove(userName);
                    friends.setGotBlockedByList(l);
                    friendsRepository.save(friends);
                    return true;
                }
                return true;
            }
            return false;
        }

    }

    @Override
    public boolean checkFriendshipStatus(FriendStatusRequestDTO requestDTO, String sessionId) {
        String userName = sessionValidate(sessionId);
        if(userName == null || userName.length() == 0) return false;
        else{
            Friends friends = friendsRepository.checkUserExistsInFriendList(userName, requestDTO.getFriendUserName());
            return (friends!=null);
        }
    }

    @Override
    public boolean checkBlockedStatus(FriendStatusRequestDTO requestDTO, String sessionId) {
        String userName = sessionValidate(sessionId);
        if(userName == null) return true; //if sessionId is invalid then true will make the friend user looking for blocked-just for reducing unwanted interaction
        else {
            Friends friends = friendsRepository.checkUserExistsInBlockList(userName, requestDTO.getFriendUserName());
            return (friends != null);
        }
    }
}
