package com.quinbook.friends.service.impl;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class KafkaAdminTopicCreator {

    public static void topicCreation(String topicName){
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        AdminClient admin  = AdminClient.create(config);
        Map<String, String> configs = new HashMap<>();
        int partitions = 1;
        short replication = 1;
        admin.createTopics(Arrays.asList(new NewTopic(topicName, partitions, replication).configs(configs)));
    }
}
