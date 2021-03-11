package com.quinbook.friends.service;

import com.quinbook.friends.dto.FriendsRequestDTO;
import com.quinbook.friends.dto.FriendsSocialDTO;
import com.quinbook.friends.entity.Friends;

public interface FriendsService {
    void addFriends(FriendsRequestDTO requestDTO,String sessionId);
    void blockFriends(FriendsRequestDTO requestDTO,String sessionId);
    void removeFriends(FriendsRequestDTO requestDTO,String sessionId);
    FriendsSocialDTO fetchUserSocial(String userName);
}
