package com.discover.discoverapi.repositories;

import com.discover.discoverapi.entities.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends Neo4jRepository<Album, Long> {
    Page<Album> findByTitleContaining(String title, Pageable pageable);

    // removes a track from the album
    @Query("MATCH (album:Album)-[edge:CONTAINS]->(track:Track)\n" +
            "WHERE ID(album) = $albumId AND ID(track) = $trackId\n" +
            "DELETE edge")
    void removeTrack(long albumId, long trackId);
}
