package com.discover.discoverapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EntityScan("com.discover.discoverapi.entities")
@EnableNeo4jRepositories("com.discover.discoverapi.repositories")
public class DiscoverApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscoverApiApplication.class, args);
    }
}
