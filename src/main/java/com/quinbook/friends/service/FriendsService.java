package com.quinbook.friends.service;

import com.quinbook.friends.dto.FriendStatusRequestDTO;
import com.quinbook.friends.dto.FriendsRequestDTO;
import com.quinbook.friends.dto.FriendsSocialDTO;
import com.quinbook.friends.entity.Friend;
import com.quinbook.friends.entity.Friends;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FriendsService {
    boolean addFriends(FriendsRequestDTO requestDTO,String sessionId);
    boolean blockFriends(FriendsRequestDTO requestDTO,String sessionId);
    boolean removeFriends(FriendsRequestDTO requestDTO,String sessionId);
    FriendsSocialDTO fetchUserSocial(String userName);
    ResponseEntity<List<Friend>> fetchFriendList(String sessionId);
    boolean unblockUser(FriendsRequestDTO requestDTO,String sessionId);
    boolean checkFriendshipStatus(FriendStatusRequestDTO requestDTO,String sessionId);
    boolean checkBlockedStatus(FriendStatusRequestDTO requestDTO,String sessionId);
}
