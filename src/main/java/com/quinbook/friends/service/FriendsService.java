package com.quinbook.friends.service;

import com.quinbook.friends.dto.FriendsRequestDTO;

public interface FriendsService {
    public void addFriends(FriendsRequestDTO requestDTO);
    public void blockFriends(FriendsRequestDTO requestDTO);
}
