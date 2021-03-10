package com.quinbook.friends;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//import static com.quinbook.friends.controller.SessionController.kafkaMethod;


@EnableFeignClients
@SpringBootApplication
public class FriendsApplication {

    public static void main(String[] args) {

        SpringApplication.run(FriendsApplication.class, args);

        //kafkaMethod();
    }


}
