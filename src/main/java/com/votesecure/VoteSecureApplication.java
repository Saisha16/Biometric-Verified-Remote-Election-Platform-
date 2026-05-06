package com.votesecure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VoteSecureApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoteSecureApplication.class, args);
    }
}
