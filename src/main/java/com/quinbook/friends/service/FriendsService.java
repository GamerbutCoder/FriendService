package com.quinbook.friends.service;

import com.quinbook.friends.dto.FriendsRequestDTO;
import com.quinbook.friends.dto.FriendsSocialDTO;
import com.quinbook.friends.entity.Friend;
import com.quinbook.friends.entity.Friends;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FriendsService {
    void addFriends(FriendsRequestDTO requestDTO,String sessionId);
    void blockFriends(FriendsRequestDTO requestDTO,String sessionId);
    void removeFriends(FriendsRequestDTO requestDTO,String sessionId);
    FriendsSocialDTO fetchUserSocial(String userName);
    ResponseEntity<List<Friend>> fetchFriendList(String sessionId);
}
