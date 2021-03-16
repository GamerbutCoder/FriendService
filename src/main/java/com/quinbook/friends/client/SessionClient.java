package com.quinbook.friends.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "session-client",url = "http://10.177.68.53:8090")
public interface SessionClient {
    @RequestMapping(method = RequestMethod.POST,path = "/fetchUserName")
    String getUserName(@RequestParam("sessionId") String sessionId);
}
