package com.discover.discoverapi.repositories;

import com.discover.discoverapi.entities.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends Neo4jRepository<Artist, Long> {
    Page<Artist> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("MATCH (theArtist:Artist)<-[edge:RECORDED_BY]-(theAlbum:Album)\n" +
            "WHERE ID(theAlbum) = $albumId AND ID(theArtist) = $artistId\n" +
            "DELETE edge")
    void deleteAlbum(long artistId, long albumId);

    @Query("MATCH (theArtist:Artist), (theAlbum:Album)\n" +
            "WHERE ID(theAlbum) = $albumId AND ID(theArtist) = $artistId\n" +
            "CREATE (theArtist)<-[edge:RECORDED_BY]-(theAlbum)")
    void addAlbum(long artistId, long albumId);

    @Query("MATCH (theArtist:Artist)-[edge:RECORDS]->(theTrack:Track)\n" +
            "WHERE ID(theTrack) = $trackId AND ID(theArtist) = $artistId\n" +
            "DELETE edge")
    void deleteTrack(long artistId, long trackId);

    @Query("MATCH (theArtist:Artist), (theTrack:Track)\n" +
            "WHERE ID(theTrack) = $trackId AND ID(theArtist) = $artistId\n" +
            "CREATE (theArtist)-[edge:RECORDS]->(theTrack)")
    void addTrack(long artistId, long trackId);
}
