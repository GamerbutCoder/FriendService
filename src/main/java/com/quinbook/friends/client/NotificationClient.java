package com.quinbook.friends.client;

import com.quinbook.friends.dto.FriendRequestAcceptanceNotification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="notification-client",url = "127.0.0.1:8085")
public interface NotificationClient {

    @RequestMapping(method = RequestMethod.POST,path = "/friendRequestAccepted")
    void sendAcceptanceNotification(@RequestBody FriendRequestAcceptanceNotification request);
}
