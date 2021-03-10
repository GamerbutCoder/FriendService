package com.quinbook.friends.service.impl;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.quinbook.friends.client.UserClient;
import com.quinbook.friends.dto.FriendProfileDTO;
import com.quinbook.friends.dto.FriendsRequestDTO;
import com.quinbook.friends.entity.Friend;
import com.quinbook.friends.entity.Friends;
import com.quinbook.friends.repository.FriendsRepository;
import com.quinbook.friends.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class FriendsServiceIMPL implements FriendsService {
    static MongoOperations mongoOperations = new MongoTemplate(MongoClients.create(),"quinbook");

    @Autowired
    private UserClient userClient;

    @Autowired
    private FriendsRepository friendsRepository;




    @Override
    public void addFriends(FriendsRequestDTO requestDTO) {

        String userName = requestDTO.getUserName();
        String friendUserName = requestDTO.getFriendUserName();
        if(userName == null || friendUserName == null || userName.length()==0 || friendUserName.length()==0){
            return;
        }
        else{
            addFriendsInDB(userName,friendUserName);
            addFriendsInDB(friendUserName,userName);
        }
    }

    @Override
    public void blockFriends(FriendsRequestDTO requestDTO) {
        String userName = requestDTO.getUserName();
        String friendUserName = requestDTO.getFriendUserName();
        if(userName == null || friendUserName == null || userName.length()==0 || friendUserName.length()==0){
            return;
        }
        else{
            Optional<Friends> optional = friendsRepository.findById(friendUserName);
            if(optional.isPresent()){
                Friends f = friendsRepository.checkUserExistsInBlockList(friendUserName,userName);
                if (f == null) {
                    Friends obj = optional.get();
                    List<String> blockedList = obj.getGotblockedByList();
                    blockedList.add(userName);
                    obj.setGotblockedByList(blockedList);
                    friendsRepository.save(obj);

//                    Friend friend = new Friend();
//                    FriendProfileDTO friendProfileDTO = userClient.getFriendProfile(userName);
//                    friend.setProfilePic(friendProfileDTO.getImg());
//                    friend.setUserName(friendUserName);
//                    friend.setFullName(friendProfileDTO.getFullName());
//                    Query query = new Query();
//                    Criteria criteria = Criteria.where("userName").is(friendUserName);
//                    query.addCriteria(criteria);
//                    Update update = new Update();
//                    update.push("gotBlockedByList", userName); //not working as intended
//                    mongoOperations.updateFirst(query, update, Friends.class);
                }
                return ;
            }
            else{
                Friends friends = new Friends();
                friends.setUserName(friendUserName);
                List<String> blockList = new ArrayList<>();
                blockList.add(userName);
                friends.setGotblockedByList(blockList);
                List<Friend> friendList = new ArrayList<>();
                friends.setFriendList(friendList);
                friendsRepository.save(friends);
                return;
            }
        }
    }

    public void addFriendsInDB(String userName,String friendUserName){
        Optional<Friends> optional = friendsRepository.findById(userName); //user exists?
        if(optional.isPresent()){
            System.out.println(userClient.getFriendProfile(friendUserName)); //friend exists?
            List<Friends> f = mongoOperations.find(new Query(where("friendList.userName").is(friendUserName)),Friends.class);
            if(f!=null || f.size()>0){
                return ;
            }
            else{

                Friend friend = new Friend();
                FriendProfileDTO friendProfileDTO = userClient.getFriendProfile(friendUserName);
                friend.setProfilePic(friendProfileDTO.getImg());
                friend.setUserName(friendUserName);
                friend.setFullName(friendProfileDTO.getFullName());
                Friends friends = optional.get();
                friends.getFriendList().add(friend);
                friendsRepository.save(friends);
//                    Query query = new Query();
//                    Criteria criteria = Criteria.where("userName").is(userName);
//                    query.addCriteria(criteria);
//                    Update update = new Update();
//                    update.push("friendList",friend);
//                    mongoOperations.updateFirst(query,update,Friends.class);
                return;
            }
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
            friends.setGotblockedByList(blockedList);
            friendsRepository.save(friends);
            return;
        }
    }
}
