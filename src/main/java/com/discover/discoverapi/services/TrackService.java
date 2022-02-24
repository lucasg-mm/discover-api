package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.repositories.TrackRepository;
import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TrackService {
    private TrackRepository trackRepository;

    // find a single track by its id
    public Track findById(long id){
        return trackRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Track of id " + id + " not found."));
    }

    // find every single stored track
    public List<Track> findAll(){
        return trackRepository.findAll();
    }

    // create a single track
    public Track create(Track toCreate){
        toCreate.setId(0);
        return trackRepository.save(toCreate);
    }

    // update a single track by id
    public Track update(long id, Track toUpdate){
        // retrieves the track
        Track retrievedTrack = findById(id);

        // updates its fields
        retrievedTrack.setArtists(toUpdate.getArtists());
        retrievedTrack.setGenres(toUpdate.getGenres());
        retrievedTrack.setTitle(toUpdate.getTitle());
        retrievedTrack.setLyrics(toUpdate.getLyrics());
        retrievedTrack.setAlbum(toUpdate.getAlbum());
        retrievedTrack.setLength(toUpdate.getLength());

        // saves it
        return trackRepository.save(retrievedTrack);
    }

    // delete a single track by its id
    public void deleteById(long id){
        if (trackRepository.existsById(id)){
            trackRepository.deleteById(id);
        }
        else{
            throw new ObjectNotFoundException("Track of id " + id + " not found.");
        }
    }
}
