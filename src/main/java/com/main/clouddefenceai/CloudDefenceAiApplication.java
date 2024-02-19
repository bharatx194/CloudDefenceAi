package com.main.clouddefenceai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CloudDefenceAiApplication {

    public static void main(String[] args) {
	SpringApplication.run(CloudDefenceAiApplication.class, args);
    }
}
