package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Genre;
import com.discover.discoverapi.repositories.GenreRepository;
import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreService {
    private GenreRepository genreRepository;

    // find a genre by id
    public Genre findById(long id){
        return genreRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Genre of id " + id + " not found."));
    }

    // find every single genre
    public List<Genre> findAll(){
        return genreRepository.findAll();
    }

    // create a single genre
    public Genre create(Genre toCreate){
        toCreate.setId(0);
        return genreRepository.save(toCreate);
    }

    // update a single genre
    public Genre update(long id, Genre toUpdate){
        // retrieves the genre
        Genre retrievedGenre = findById(id);

        // updates retrieved genre's properties
        retrievedGenre.setArtists(toUpdate.getArtists());
        retrievedGenre.setName(toUpdate.getName());
        retrievedGenre.setTracks(toUpdate.getTracks());
        retrievedGenre.setAlbums(toUpdate.getAlbums());

        // saves
        return genreRepository.save(retrievedGenre);
    }

    // deletes a single genre
    public void deleteById(long id){
        if (genreRepository.existsById(id)){
            genreRepository.deleteById(id);

        }
        else{
            throw new ObjectNotFoundException("Genre of id " + id + " not found.");
        }
    }
}
