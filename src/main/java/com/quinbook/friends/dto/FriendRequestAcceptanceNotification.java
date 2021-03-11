package com.quinbook.friends.dto;

import com.quinbook.friends.entity.Friend;
import lombok.Data;

@Data
public class FriendRequestAcceptanceNotification {
    private String eventType;
    private Friend acceptedBy;
    private String whose;
}
