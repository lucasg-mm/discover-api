package com.discover.discoverapi.repositories;

import com.discover.discoverapi.entities.AppUser;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserRepository extends Neo4jRepository<AppUser, Long> {
    List<AppUser> findByUsername(String username);
}
