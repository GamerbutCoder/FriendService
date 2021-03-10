package com.quinbook.friends.dto;

import com.quinbook.friends.entity.Friend;
import com.quinbook.friends.entity.Policy;
import lombok.Data;

import java.util.List;

@Data
public class FriendsSocialDTO {
    private String userName;
    private List<Friend> friendList;
    private List<String> gotBlockedByList;
    private Policy policy;
}
