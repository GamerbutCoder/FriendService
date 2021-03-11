package com.quinbook.friends.controller;

import com.quinbook.friends.dto.FriendsRequestDTO;
import com.quinbook.friends.dto.FriendsSocialDTO;
import com.quinbook.friends.entity.Friend;
import com.quinbook.friends.repository.FriendsRepository;
import com.quinbook.friends.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class FriendController {

    @Autowired
    private FriendsService friendsService;

    @PostMapping("/addFriends")
    public void addFriend( @RequestBody  FriendsRequestDTO requestDTO,@RequestHeader("sessionId") String sessionId){
        friendsService.addFriends(requestDTO,sessionId);
    }

    @PostMapping("/blockUser")
    public void blockFriend(@RequestBody FriendsRequestDTO requestDTO,@RequestHeader("sessionId") String sessionId){
        friendsService.blockFriends(requestDTO,sessionId);
    }

    @PostMapping("/removeFriend")
    public void removeFriend(@RequestBody FriendsRequestDTO requestDTO,@RequestHeader("sessionId") String sessionId){
        friendsService.removeFriends(requestDTO,sessionId);
    }

    @PostMapping("/fetchUserSocial")
    public FriendsSocialDTO fetchUserSocial(@RequestParam String userName){
        return friendsService.fetchUserSocial(userName);
    }

    @GetMapping("/fetchFriendList")
    public ResponseEntity<List<Friend>> fetchFriendList(@RequestHeader("sessionId") String sessionId){
        return friendsService.fetchFriendList(sessionId);
    }



}
