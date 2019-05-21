package com.hsbc.cloudchatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;


//@EnableDiscoveryClient
@EnableCaching
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.hsbc.cloudchatbot"})
@EnableAutoConfiguration
//@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})  //可以去掉Security
public class CloudChatbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudChatbotApplication.class, args);
	}

}
