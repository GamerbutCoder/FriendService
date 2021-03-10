package com.quinbook.friends.controller;

import com.quinbook.friends.dto.FriendsRequestDTO;
import com.quinbook.friends.dto.FriendsSocialDTO;
import com.quinbook.friends.repository.FriendsRepository;
import com.quinbook.friends.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class FriendController {

    @Autowired
    private FriendsService friendsService;

    @PostMapping("/addFriends")
    public void addFriend( @RequestBody  FriendsRequestDTO requestDTO){
        friendsService.addFriends(requestDTO);
    }

    @PostMapping("/blockUser")
    public void blockFriend(@RequestBody FriendsRequestDTO requestDTO){
        friendsService.blockFriends(requestDTO);
    }

    @PostMapping("/removeFriend")
    public void removeFriend(@RequestBody FriendsRequestDTO requestDTO){
        friendsService.removeFriends(requestDTO);
    }

    @PostMapping("/fetchUserSocial")
    public FriendsSocialDTO fetchUserSocial(@RequestParam String userName){
        return friendsService.fetchUserSocial(userName);
    }

}
