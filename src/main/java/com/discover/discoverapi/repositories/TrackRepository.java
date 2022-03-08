package com.discover.discoverapi.repositories;

import com.discover.discoverapi.entities.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
    Page<Track> findByTitleContaining(String title, Pageable pageable);
}
