package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.repositories.AlbumRepository;
import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;

    // find all
    public List<Album> findAll(){
        return albumRepository.findAll();
    }

    // find by id
    public Album findById(long id){
        return albumRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Album of id " + id + " not found."));
    }

    // update by id

    // create

    public Album create(Album album){
        return albumRepository.save(album);
    }

    // delete
}
