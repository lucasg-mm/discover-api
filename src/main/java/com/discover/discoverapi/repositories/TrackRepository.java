package com.discover.discoverapi.repositories;

import com.discover.discoverapi.entities.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TrackRepository extends Neo4jRepository<Track, Long> {
    Page<Track> findByTitleContaining(String title, Pageable pageable);
}
