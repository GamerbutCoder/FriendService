package com.quinbook.friends.controller;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
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
//    List<String> topicList = new ArrayList<>();
//    @KafkaListener(topics = "session-details")
//    public void listenMessage(Message<String> message){
//        System.out.println(message.getPayload() + message.getHeaders());
//        String userName = message.getHeaders().get("kafka_receivedMessageKey").toString();
//        String[] arr = message.getPayload().split(" ");
//        System.out.println(Arrays.toString(arr) + " "+ userName);
////        if(arr.length == 2){
////            sessionService.updateSession(userName,arr[0],arr[1]);
////        }
////        else{
////            sessionService.deleteSession(arr[0]);
////        }
//    }
    public static Properties ConsumerConfig(){
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "10.177.68.66:9092");
        props.setProperty("group.id", "test");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }

    public static void kafkaMethod(){
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(ConsumerConfig());
        consumer.subscribe(Arrays.asList("session-details"));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        }



        //consumer.close();
    }

}
