package com.quinbook.friends.controller;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.quinbook.friends.entity.Login;
import com.quinbook.friends.repository.LoginDao;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@RestController
public class SessionController {
    List<String> topicList = new ArrayList<>();

    @Autowired
    private LoginDao loginDao;

    @KafkaListener(topics = "session")
    public void listenMessage(Message<String> message) {

        String[] arr = message.getPayload().split(" ");
        Login login = new Login();
        System.out.println(Arrays.toString(arr));
        login.setSessionId(arr[1]);
        login.setUserName(arr[0]);
        loginDao.save(login);
    }
}


