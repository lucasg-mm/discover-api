package com.discover.discoverapi.repositories;

import com.discover.discoverapi.entities.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends Neo4jRepository<Artist, Long> {
    Page<Artist> findByNameContaining(String name, Pageable pageable);
}
