package com.lv.reg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class RegApplication {

    public static void main(String[] args) {
        //ApiContextInitializer.init(); // telegram bot init
        SpringApplication.run(RegApplication.class, args);
    }
}
