package com.quinbook.friends.service;

import com.quinbook.friends.dto.FriendsRequestDTO;

public interface FriendsService {
    void addFriends(FriendsRequestDTO requestDTO);
    void blockFriends(FriendsRequestDTO requestDTO);
    void removeFriends(FriendsRequestDTO requestDTO);
}
