package com.discover.discoverapi.repositories;

import com.discover.discoverapi.entities.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Page<Artist> findByNameContaining(String name, Pageable pageable);
}
