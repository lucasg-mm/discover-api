package com.discover.discoverapi.repositories;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface AlbumRepository extends Neo4jRepository<Album, Long> {
    Page<Album> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // removes a track from the album
    @Query("MATCH (theAlbum:Album)-[edge:CONTAINS]->(theTrack:Track)\n" +
            "WHERE ID(theAlbum) = $albumId AND ID(theTrack) = $trackId\n" +
            "DELETE edge")
    void removeTrack(long albumId, long trackId);

    // adds a track to the album
    @Query("MATCH (theAlbum:Album), (theTrack:Track)\n" +
            "WHERE ID(theAlbum) = $albumId AND ID(theTrack) = $trackId\n" +
            "CREATE (theAlbum)-[:CONTAINS]->(theTrack)")
    void addTrack(long albumId, long trackId);
}
