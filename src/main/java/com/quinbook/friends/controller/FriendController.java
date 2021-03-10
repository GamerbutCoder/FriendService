package com.quinbook.friends.controller;

import com.quinbook.friends.dto.FriendsRequestDTO;
import com.quinbook.friends.repository.FriendsRepository;
import com.quinbook.friends.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
