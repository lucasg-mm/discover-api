package com.discover.discoverapi.repositories;

import com.discover.discoverapi.entities.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends Neo4jRepository<Genre, Long> {
    Page<Genre> findByNameContaining(String name, Pageable pageable);
}
