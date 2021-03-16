package com.quinbook.friends.repository;

import com.quinbook.friends.entity.Friends;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface FriendsRepository extends MongoRepository<Friends,String> {
   // @Query(value = "")
//    @M(find = {}, update = "{$push : {'offFeatures' : ?0}}", collection = "userPreference", multi = true)
//    public void offFeatures(String feature) {
//
//    }
    @Query(value = "{ 'userName':?0 , 'gotBlockedByList':?1 }")
    Friends checkUserExistsInBlockList( String friendName,String userName);

    @Query(value = "{ 'userName':?0 , 'friendList.?1':{$exists:true} }")
    Friends checkUserExistsInFriendList(String  userName ,String friendName);
}
