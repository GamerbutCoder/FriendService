package com.quinbook.friends.service;

import com.quinbook.friends.dto.FriendsRequestDTO;
import com.quinbook.friends.dto.FriendsSocialDTO;
import com.quinbook.friends.entity.Friends;

public interface FriendsService {
    void addFriends(FriendsRequestDTO requestDTO);
    void blockFriends(FriendsRequestDTO requestDTO);
    void removeFriends(FriendsRequestDTO requestDTO);
    FriendsSocialDTO fetchUserSocial(String userName);
}
