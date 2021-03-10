package com.quinbook.friends.service.impl;

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
                Friends friends = friendsRepository.checkUserExistsInBlockList(friendUserName,userName);
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
                friendsRepository.save(friends);
            }
            removeFriendsFromDB(userName,friendUserName);
            removeFriendsFromDB(friendUserName,userName);
            return;
        }
    }

    @Override
    public void removeFriends(FriendsRequestDTO requestDTO) {
        String userName = requestDTO.getUserName();
        String friendUserName = requestDTO.getFriendUserName();
        if(userName == null || friendUserName == null || userName.length()==0 || friendUserName.length()==0){
            return;
        }
        else{
            removeFriendsFromDB(userName,friendUserName);
            removeFriendsFromDB(friendUserName,userName);
        }

    }

    private void addFriendsInDB(String userName, String friendUserName){
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
            friends.setGotBlockedByList(blockedList);
            friendsRepository.save(friends);
        }
        return ;
    }

    private void removeFriendsFromDB(String userName,String friendUserName){
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
                mongoOperations.updateFirst(query,update,Friends.class);

            }
            catch (Exception e){

            }
        }
    }
}
