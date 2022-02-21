package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Artist;
import com.discover.discoverapi.repositories.ArtistRepository;
import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;

    // find by id
    public Artist findById(long id){
        return artistRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Artist of id " + id + " not found."));
    }

    // find all artists
    public List<Artist> findAll(){
        return artistRepository.findAll();
    }

    // create an artist
    public Artist create(Artist toCreate){
        toCreate.setId(0);
        return artistRepository.save(toCreate);
    }

    // update an artist
    public Artist update(long id, Artist toUpdate){
        // retrieves the artist with the given id
        Artist retrievedArtist = findById(id);

        // updates retrieved artist properties
        retrievedArtist.setImageURL(toUpdate.getImageURL());
        retrievedArtist.setName(toUpdate.getName());
        retrievedArtist.setGenres(toUpdate.getGenres());
        retrievedArtist.setAlbums(toUpdate.getAlbums());
        retrievedArtist.setTracks(toUpdate.getTracks());

        // saves artist
        return artistRepository.save(retrievedArtist);
    }

    // delete an artist by id
    public void deleteById(long id){
        if (artistRepository.existsById(id)){
            artistRepository.deleteById(id);
        }
        else{
            throw new ObjectNotFoundException("Artist of id " + id + " not found.");
        }
    }
}
