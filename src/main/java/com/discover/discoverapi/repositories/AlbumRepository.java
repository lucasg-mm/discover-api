package com.discover.discoverapi.repositories;

import com.discover.discoverapi.entities.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
}
