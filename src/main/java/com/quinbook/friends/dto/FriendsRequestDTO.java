package com.quinbook.friends.dto;

import com.quinbook.friends.entity.Friend;
import lombok.Data;

import java.util.List;

@Data
public class FriendsRequestDTO {
    private String userName;
    private String friendUserName;
    private Friend selfDetails;
}
