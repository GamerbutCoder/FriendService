package com.quinbook.friends;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;



@EnableFeignClients
@SpringBootApplication
public class FriendsApplication {

    public static void main(String[] args) {

        SpringApplication.run(FriendsApplication.class, args);

    }


}
