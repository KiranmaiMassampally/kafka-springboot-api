package com.springKafka.oder_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class kafkaTopicConfig {

@Value("${spring.kafka.topic.name}")
private String topicName;
//SpringBean for kafka topic
@Bean
public NewTopic topic(){
    //topic builder to create instance of new topic(if want to create no of parttitions give .aprtions() api
    return TopicBuilder.name(topicName)
             .build();
}


}
