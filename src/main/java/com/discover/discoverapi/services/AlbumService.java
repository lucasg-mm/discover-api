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
    public Album update(long id, Album toUpdate){
        // finds the album to be updated
        Album foundAlbum = findById(id);

        // updates every field
        foundAlbum.setLabel(toUpdate.getLabel());
        foundAlbum.setTitle(toUpdate.getTitle());
        foundAlbum.setCoverArtUrl(toUpdate.getCoverArtUrl());
        foundAlbum.setTracks(toUpdate.getTracks());
        foundAlbum.setLength(toUpdate.getLength());
        foundAlbum.setGenres(toUpdate.getGenres());
        foundAlbum.setReleaseDate(toUpdate.getReleaseDate());
        foundAlbum.setArtists(toUpdate.getArtists());

        // saves
        return albumRepository.save(foundAlbum);
    }

    // create
    public Album create(Album album){
        album.setId(0);  // shouldn't be null?
        return albumRepository.save(album);
    }

    // delete by id
    public void deleteById(long id){
        if (albumRepository.existsById(id)){
            albumRepository.deleteById(id);
        }
        else{
            throw new ObjectNotFoundException("Album of id " + id + " not found.");
        }
    }
}
