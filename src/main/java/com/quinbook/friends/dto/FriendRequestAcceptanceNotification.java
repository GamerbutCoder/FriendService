package com.quinbook.friends.dto;

import com.quinbook.friends.entity.Friend;
import lombok.Data;

@Data
public class FriendRequestAcceptancdNotification {
    private String eventType;
    private Friend acceptedBy;
}
