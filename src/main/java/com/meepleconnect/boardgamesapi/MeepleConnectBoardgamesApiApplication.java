package com.meepleconnect.boardgamesapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class MeepleConnectBoardgamesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeepleConnectBoardgamesApiApplication.class, args);
        System.out.println("🎲 MeepleConnect API is gestart en draait op http://localhost:8080/");
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        System.out.println("🕒 Tijdzone ingesteld op UTC");
    }
}
