package com.quinbook.friends.controller;

import com.quinbook.friends.dto.FriendStatusRequestDTO;
import com.quinbook.friends.dto.FriendsRequestDTO;
import com.quinbook.friends.dto.FriendsSocialDTO;
import com.quinbook.friends.entity.Friend;
import com.quinbook.friends.repository.FriendsRepository;
import com.quinbook.friends.repository.LoginDao;
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

    @Autowired
    private LoginDao loginDao;

    @PostMapping("/addFriends")
    public boolean addFriend( @RequestBody  FriendsRequestDTO requestDTO,@RequestHeader("sessionId") String sessionId){
        return friendsService.addFriends(requestDTO,sessionId);
    }

    @PostMapping("/blockUser")
    public boolean blockFriend(@RequestBody FriendsRequestDTO requestDTO,@RequestHeader("sessionId") String sessionId){
        return friendsService.blockFriends(requestDTO,sessionId);
    }

    @PostMapping("/unblockUser")
    public boolean unblockFriend(@RequestBody FriendsRequestDTO requestDTO,@RequestHeader("sessionId") String sessionId){
        return friendsService.unblockUser(requestDTO,sessionId);
    }

    @PostMapping("/removeFriend")
    public boolean removeFriend(@RequestBody FriendsRequestDTO requestDTO,@RequestHeader("sessionId") String sessionId){
        return friendsService.removeFriends(requestDTO,sessionId);
    }

    @PostMapping("/fetchUserSocial")
    public FriendsSocialDTO fetchUserSocial(@RequestParam String userName){
        return friendsService.fetchUserSocial(userName);
    }

    @GetMapping("/fetchFriendList")
    public ResponseEntity<List<Friend>> fetchFriendList(@RequestHeader("sessionId") String sessionId){
        return friendsService.fetchFriendList(sessionId);
    }

   @PostMapping("/isMyFriend")
    public boolean checkMyfriendList(@RequestBody FriendStatusRequestDTO requestDTO,@RequestHeader("sessionId") String sessionId){
        return friendsService.checkFriendshipStatus(requestDTO,sessionId);
   }

   @PostMapping("/amIBlocked")
    public boolean checkAmIblocked(@RequestBody FriendStatusRequestDTO requestDTO,@RequestHeader("sessionId") String sessionId){
        return friendsService.checkBlockedStatus(requestDTO,sessionId);
   }










}
