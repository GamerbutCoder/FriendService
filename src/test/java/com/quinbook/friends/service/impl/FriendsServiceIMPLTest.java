package com.quinbook.friends.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quinbook.friends.client.NotificationClient;
import com.quinbook.friends.client.SessionClient;
import com.quinbook.friends.client.UserClient;
import com.quinbook.friends.dto.*;
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
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import sun.reflect.Reflection;
import sun.rmi.runtime.Log;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.quinbook.friends.constants.ConstantStrings.FRIEND_REQUEST_ACCEPT;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

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


//    @AfterEach
//    public void teardown(){
//
//    }

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

        assertNotNull(response);

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

    @Mock
    private NotificationClient notificationClient;

    @Mock
    private UserClient userClient;

    @Mock
    private MongoOperations mongoOperations;

    @Test
    void addFriendsAnnBlockFriends() throws IOException{
        FriendsRequestDTO requestDTO = new FriendsRequestDTO();
        Friend friend = new Friend();
        friend.setFullName("test user1");
        friend.setProfilePic("some img");
        friend.setUserName("test1");
        requestDTO.setFriendUserName("test2");
        requestDTO.setUserName("test1");
        requestDTO.setSelfDetails(null);

        String sessionId = null;

        Login login = new Login();
        login.setUserName("test1");
        login.setSessionId(sessionId);

        Mockito.when(loginDao.findUserById(sessionId)).thenReturn(null);

        assertEquals(false,friendsServiceIMPL.addFriends(requestDTO,sessionId));
        assertEquals(false,friendsServiceIMPL.blockFriends(requestDTO,sessionId));
        assertEquals(false,friendsServiceIMPL.removeFriends(requestDTO,sessionId));
        assertEquals(false,friendsServiceIMPL.unblockUser(requestDTO,sessionId));

        Mockito.when(loginDao.findUserById(sessionId)).thenReturn(login);

        assertEquals(false,friendsServiceIMPL.addFriends(requestDTO,sessionId));
        assertEquals(false,friendsServiceIMPL.blockFriends(requestDTO,sessionId));
        assertEquals(false,friendsServiceIMPL.unblockUser(requestDTO,sessionId));


        requestDTO.setSelfDetails(friend);
        FriendProfileDTO friendProfileDTO = new FriendProfileDTO();
        friendProfileDTO.setFullName("test user 2");
        friendProfileDTO.setImg("some img");
        FriendProfileDTO friendProfileDTO1 = new FriendProfileDTO();
        friendProfileDTO1.setImg("some img");
        friendProfileDTO1.setFullName("test user 1");
        Mockito.when(userClient.getFriendProfile("test2")).thenReturn(friendProfileDTO);
        Mockito.when(userClient.getFriendProfile("test1")).thenReturn(friendProfileDTO1);
        assertEquals(true,friendsServiceIMPL.addFriends(requestDTO,sessionId));
        Mockito.when(friendsRepository.save(Mockito.any(Friends.class))).thenReturn(null);

        assertEquals(true,friendsServiceIMPL.addFriends(requestDTO,sessionId));
        assertEquals(true,friendsServiceIMPL.blockFriends(requestDTO,sessionId));

        Mockito.when(friendsRepository.findById("test1")).thenReturn(null);

        sessionId = "7bf9bb28b351af47133e44ad304da8fce9ffc286062f35d04e5a2bb1477766f3";
        assertEquals(false,friendsServiceIMPL.addFriends(requestDTO,sessionId));

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

        //sessionId = "7bf9bb28b351af47133e44ad304da8fce9ffc286062f35d04e5a2bb1477766f3";

        //Mockito.when(friendsRepository.findById("test1")).thenReturn(Optional.of(friends));
        Mockito.when(friendsRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(friends));
        Mockito.when(loginDao.findUserById(sessionId)).thenReturn(login);
        List<Friends> list = new ArrayList<>();
        list.add(friends);

        Mockito.when(mongoOperations.find(new Query(where("friendList.userName").is("test2")),Friends.class)).thenReturn(list);

        assertEquals(true,friendsServiceIMPL.addFriends(requestDTO,sessionId));
        assertEquals(true,friendsServiceIMPL.blockFriends(requestDTO,sessionId));
        assertEquals(true,friendsServiceIMPL.removeFriends(requestDTO,sessionId));
        assertEquals(true,friendsServiceIMPL.unblockUser(requestDTO,sessionId));

        //Mockito.when(friendsRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(null));




    }
}