package com.discover.discoverapi.repositories;

import com.discover.discoverapi.entities.AppUser;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserRepository extends Neo4jRepository<AppUser, Long> {
    List<AppUser> findByUsername(String username);

    @Query("MATCH (user:AppUser)-[edge:LIKES]->(album:Album)\n" +
            "WHERE ID(album) = $albumId AND ID(user) = $userId\n" +
            "DELETE edge")
    void removeAlbumFromLiked(long userId, long albumId);

    @Query("MATCH (user:AppUser), (album:Album)\n" +
            "WHERE ID(album) = $albumId AND ID(user) = $userId\n" +
            "CREATE (user)-[:LIKES]->(album)")
    void  addAlbumToLiked(long userId, long albumId);
}
