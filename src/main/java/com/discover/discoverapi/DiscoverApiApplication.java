package com.discover.discoverapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.discover.discoverapi.entities")
public class DiscoverApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscoverApiApplication.class, args);
    }
}
