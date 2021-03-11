package com.quinbook.friends.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Login")
public class Login implements Serializable {

    @Id
    private String sessionId;
    private String userName;
}