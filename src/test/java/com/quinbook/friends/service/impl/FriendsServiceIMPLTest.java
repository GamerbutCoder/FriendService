package com.quinbook.friends.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quinbook.friends.client.SessionClient;
import com.quinbook.friends.dto.FriendStatusRequestDTO;
import com.quinbook.friends.dto.FriendsSocialDTO;
import com.quinbook.friends.entity.Friend;
import com.quinbook.friends.entity.Friends;
import com.quinbook.friends.entity.Login;
import com.quinbook.friends.entity.Policy;
import com.quinbook.friends.repository.FriendsRepository;
import com.quinbook.friends.repository.LoginDao;
import org.apache.zookeeper.Op;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import sun.reflect.Reflection;
import sun.rmi.runtime.Log;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class FriendsServiceIMPLTest {


    @InjectMocks
    private FriendsServiceIMPL friendsServiceIMPL;

    @Mock
    private SessionClient sessionClient;

    @Mock
    private FriendsRepository friendsRepository;

    @Mock
    private LoginDao loginDao;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
    }
    @AfterEach
    public void teardown(){

    }

    @Test
    void fetchUserSocial() throws IOException {
        String userName = "Test User";
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String,Object> searchTermMockObject=objectMapper.readValue(
                new URL("file:src/test/resources/friendListMock.mock"),Map.class);

        Friends friends = new Friends();
        friends.setFriendList((List)searchTermMockObject.get("friendList"));
        Policy policy = new Policy();
        Map<String,Object> policyMock = (Map<String,Object>)searchTermMockObject.get("policy");
        policy.setFriendList((String)policyMock.get("friendList"));
        policy.setFeed((String) policyMock.get("feed"));
        policy.setProfilePic((String) policyMock.get("profilePic"));
        friends.setPolicy(policy);
        friends.setGotBlockedByList((List)searchTermMockObject.get("gotBlockedByList"));
        friends.setUserName((String) searchTermMockObject.get("userName"));
        Mockito.when(friendsRepository.findById("Test User")).thenReturn(Optional.of(friends));
        FriendsSocialDTO response = friendsServiceIMPL.fetchUserSocial(userName);
    }



    @Test
    void fetchFriendList() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String,Object> searchTermMockObject=objectMapper.readValue(
                new URL("file:src/test/resources/friendListMock.mock"),Map.class);

        Friends friends = new Friends();
        friends.setFriendList((List)searchTermMockObject.get("friendList"));
        Policy policy = new Policy();
        Map<String,Object> policyMock = (Map<String,Object>)searchTermMockObject.get("policy");
        policy.setFriendList((String)policyMock.get("friendList"));
        policy.setFeed((String) policyMock.get("feed"));
        policy.setProfilePic((String) policyMock.get("profilePic"));
        friends.setPolicy(policy);
        friends.setGotBlockedByList((List)searchTermMockObject.get("gotBlockedByList"));
        friends.setUserName((String) searchTermMockObject.get("userName"));

        String sessionId = "7bf9bb28b351af47133e44ad304da8fce9ffc286062f35d04e5a2bb1477766f3";

        Mockito.when(sessionClient.getUserName(sessionId)).thenReturn("Test User");

        Mockito.when(friendsRepository.findById("Test User")).thenReturn(Optional.of(friends));

        ResponseEntity<List<Friend>> response = friendsServiceIMPL.fetchFriendList(sessionId);

        System.out.println(response);

    }


    @Test
    void checkFriendshipStatus() throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String,Object> searchTermMockObject=objectMapper.readValue(
                new URL("file:src/test/resources/friendListMock.mock"),Map.class);

        Friends friends = new Friends();
        friends.setFriendList((List)searchTermMockObject.get("friendList"));
        Policy policy = new Policy();
        Map<String,Object> policyMock = (Map<String,Object>)searchTermMockObject.get("policy");
        policy.setFriendList((String)policyMock.get("friendList"));
        policy.setFeed((String) policyMock.get("feed"));
        policy.setProfilePic((String) policyMock.get("profilePic"));
        friends.setPolicy(policy);
        friends.setGotBlockedByList((List)searchTermMockObject.get("gotBlockedByList"));
        friends.setUserName((String) searchTermMockObject.get("userName"));

        String sessionId = "7bf9bb28b351af47133e44ad304da8fce9ffc286062f35d04e5a2bb1477766f3";

        Mockito.when(friendsRepository.checkUserExistsInFriendList("user1","frienduser1")).thenReturn(friends);

        Login login = new Login();
        login.setSessionId(sessionId);
        login.setUserName("user1");
        Mockito.when(loginDao.findUserById(sessionId)).thenReturn(login);

        FriendStatusRequestDTO req = new FriendStatusRequestDTO();
        req.setFriendUserName("frienduser1");

        assertEquals(friendsServiceIMPL.checkFriendshipStatus(req,sessionId),true);


    }

    @Test
    void checkFriendshipStatus1() throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String,Object> searchTermMockObject=objectMapper.readValue(
                new URL("file:src/test/resources/friendListMock.mock"),Map.class);

        Friends friends = new Friends();
        friends.setFriendList((List)searchTermMockObject.get("friendList"));
        Policy policy = new Policy();
        Map<String,Object> policyMock = (Map<String,Object>)searchTermMockObject.get("policy");
        policy.setFriendList((String)policyMock.get("friendList"));
        policy.setFeed((String) policyMock.get("feed"));
        policy.setProfilePic((String) policyMock.get("profilePic"));
        friends.setPolicy(policy);
        friends.setGotBlockedByList((List)searchTermMockObject.get("gotBlockedByList"));
        friends.setUserName((String) searchTermMockObject.get("userName"));

        String sessionId = "7bf9bb28b351af47133e44ad304da8fce9ffc286062f35d04e5a2bb1477766f3";

        Mockito.when(friendsRepository.checkUserExistsInFriendList("user1","frienduser1")).thenReturn(null);

        Login login = new Login();
        login.setSessionId(sessionId);
        login.setUserName("user1");
        Mockito.when(loginDao.findUserById(sessionId)).thenReturn(login);

        FriendStatusRequestDTO req = new FriendStatusRequestDTO();
        req.setFriendUserName("frienduser1");

        assertEquals(friendsServiceIMPL.checkFriendshipStatus(req,sessionId),false);

    }

    @Test
    void checkBlockedStatus() throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String,Object> searchTermMockObject=objectMapper.readValue(
                new URL("file:src/test/resources/friendListMock.mock"),Map.class);

        Friends friends = new Friends();
        friends.setFriendList((List)searchTermMockObject.get("friendList"));
        Policy policy = new Policy();
        Map<String,Object> policyMock = (Map<String,Object>)searchTermMockObject.get("policy");
        policy.setFriendList((String)policyMock.get("friendList"));
        policy.setFeed((String) policyMock.get("feed"));
        policy.setProfilePic((String) policyMock.get("profilePic"));
        friends.setPolicy(policy);
        friends.setGotBlockedByList((List)searchTermMockObject.get("gotBlockedByList"));
        friends.setUserName((String) searchTermMockObject.get("userName"));

        String sessionId = "7bf9bb28b351af47133e44ad304da8fce9ffc286062f35d04e5a2bb1477766f3";

        Mockito.when(friendsRepository.checkUserExistsInBlockList("user1","frienduser1")).thenReturn(friends);

        Login login = new Login();
        login.setSessionId(sessionId);
        login.setUserName("user1");
        Mockito.when(loginDao.findUserById(sessionId)).thenReturn(login);

        FriendStatusRequestDTO req = new FriendStatusRequestDTO();
        req.setFriendUserName("frienduser1");

        assertEquals(friendsServiceIMPL.checkBlockedStatus(req,sessionId),true);

    }

    @Test
    void checkBlockedStatus1() throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String,Object> searchTermMockObject=objectMapper.readValue(
                new URL("file:src/test/resources/friendListMock.mock"),Map.class);

        Friends friends = new Friends();
        friends.setFriendList((List)searchTermMockObject.get("friendList"));
        Policy policy = new Policy();
        Map<String,Object> policyMock = (Map<String,Object>)searchTermMockObject.get("policy");
        policy.setFriendList((String)policyMock.get("friendList"));
        policy.setFeed((String) policyMock.get("feed"));
        policy.setProfilePic((String) policyMock.get("profilePic"));
        friends.setPolicy(policy);
        friends.setGotBlockedByList((List)searchTermMockObject.get("gotBlockedByList"));
        friends.setUserName((String) searchTermMockObject.get("userName"));

        String sessionId = "7bf9bb28b351af47133e44ad304da8fce9ffc286062f35d04e5a2bb1477766f3";

        Mockito.when(friendsRepository.checkUserExistsInBlockList("user1","frienduser1")).thenReturn(friends);

        Login login = new Login();
        login.setSessionId(sessionId);
        login.setUserName(null);
        Mockito.when(loginDao.findUserById(sessionId)).thenReturn(login);

        FriendStatusRequestDTO req = new FriendStatusRequestDTO();
        req.setFriendUserName("frienduser1");

        assertEquals(friendsServiceIMPL.checkBlockedStatus(req,sessionId),true);

    }

    @Test
    void checkBlockedStatus2() throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String,Object> searchTermMockObject=objectMapper.readValue(
                new URL("file:src/test/resources/friendListMock.mock"),Map.class);

        Friends friends = new Friends();
        friends.setFriendList((List)searchTermMockObject.get("friendList"));
        Policy policy = new Policy();
        Map<String,Object> policyMock = (Map<String,Object>)searchTermMockObject.get("policy");
        policy.setFriendList((String)policyMock.get("friendList"));
        policy.setFeed((String) policyMock.get("feed"));
        policy.setProfilePic((String) policyMock.get("profilePic"));
        friends.setPolicy(policy);
        friends.setGotBlockedByList((List)searchTermMockObject.get("gotBlockedByList"));
        friends.setUserName((String) searchTermMockObject.get("userName"));

        String sessionId = "7bf9bb28b351af47133e44ad304da8fce9ffc286062f35d04e5a2bb1477766f3";

        Mockito.when(friendsRepository.checkUserExistsInBlockList("user1","frienduser1")).thenReturn(null);

        Login login = new Login();
        login.setSessionId(sessionId);
        login.setUserName("user1");
        Mockito.when(loginDao.findUserById(sessionId)).thenReturn(login);

        FriendStatusRequestDTO req = new FriendStatusRequestDTO();
        req.setFriendUserName("frienduser1");

        assertEquals(false,friendsServiceIMPL.checkBlockedStatus(req,sessionId));

    }
}