package com.quinbook.friends.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;
@Data
@Document(value = "friends")
public class Friends {
    @MongoId
    private String userName;
    private List<Friend> friendList;
    private List<String> gotblockedByList;

}
