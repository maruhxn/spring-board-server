package com.maruhxn.boardserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BoardServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoardServerApplication.class, args);
    }

}
