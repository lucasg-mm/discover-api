package com.discover.discoverapi.repositories;

import com.discover.discoverapi.entities.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends Neo4jRepository<Genre, Long> {
    Page<Genre> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("MATCH (theGenre:Genre)-[edge:REPRESENTED_BY]->(theAlbum:Album)\n" +
            "WHERE ID(theAlbum) = $albumId AND ID(theGenre) = $genreId\n" +
            "DELETE edge")
    void deleteAlbum(long genreId, long albumId);

    @Query("MATCH (theAlbum:Album), (theGenre:Genre)\n" +
            "WHERE ID(theAlbum) = $albumId AND ID(theGenre) = $genreId\n" +
            "CREATE (theAlbum)<-[edge:REPRESENTED_BY]-(theGenre)")
    void addAlbum(long genreId, long albumId);

    @Query("MATCH (theGenre:Genre)-[edge:REPRESENTED_BY]->(theTrack:Track)\n" +
            "WHERE ID(theTrack) = $trackId AND ID(theGenre) = $genreId\n" +
            "DELETE edge")
    void deleteTrack(long genreId, long trackId);

    @Query("MATCH (theGenre:Genre), (theTrack:Track)\n" +
            "WHERE ID(theTrack) = $trackId AND ID(theGenre) = $genreId\n" +
            "CREATE (theGenre)-[edge:REPRESENTED_BY]->(theTrack)")
    void addTrack(long genreId, long trackId);

    @Query("MATCH (theGenre:Genre)-[edge:REPRESENTED_BY]->(theArtist:Artist)\n" +
            "WHERE ID(theArtist) = $artistId AND ID(theGenre) = $genreId\n" +
            "DELETE edge")
    void deleteArtist(long genreId, long artistId);

    @Query("MATCH (theArtist:Artist), (theGenre:Genre)\n" +
            "WHERE ID(theGenre) = $genreId AND ID(theArtist) = $artistId\n" +
            "CREATE (theGenre)-[edge:REPRESENTED_BY]->(theArtist)")
    void addArtist(long genreId, long artistId);
}
