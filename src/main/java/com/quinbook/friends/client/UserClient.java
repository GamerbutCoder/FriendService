package com.quinbook.friends.client;

import com.quinbook.friends.dto.FriendProfileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-name-fetching", url="http://10.177.68.53:8081")
public interface UserClient {
    @RequestMapping(method = RequestMethod.GET,path="/userName")
    FriendProfileDTO getFriendProfile(@RequestParam("userName") String userName);
}
