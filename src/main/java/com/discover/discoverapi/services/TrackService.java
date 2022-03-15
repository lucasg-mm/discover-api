package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.repositories.TrackRepository;
import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@Service
@AllArgsConstructor
public class TrackService {
    private TrackRepository trackRepository;

    // find a single track by its id
    @Transactional
    public Track findById(long id){
        return trackRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Track of id " + id + " not found."));
    }

    // find every single stored track
    @Transactional
    public List<Track> findAll(){
        return trackRepository.findAll();
    }

    // create a single track
    @Transactional
    public Track create(Track toCreate){
        toCreate.setId(0);
        return trackRepository.save(toCreate);
    }

    // update a single track by id
    @Transactional
    public Track update(long id, Track toUpdate){
        // retrieves the track
        Track retrievedTrack = findById(id);

        // updates its fields
        retrievedTrack.setTitle(toUpdate.getTitle());
        retrievedTrack.setLength(toUpdate.getLength());

        // saves it
        return trackRepository.save(retrievedTrack);
    }

    // delete a single track by its id
    @Transactional
    public void deleteById(long id){
        if (trackRepository.existsById(id)){
            trackRepository.deleteById(id);
        }
        else{
            throw new ObjectNotFoundException("Track of id " + id + " not found.");
        }
    }

    // find tracks with a title that contains the 'title' param, and returns it in a
    // paginated way
    @Transactional
    public Map<String, Object> findByTitleContaining(
            @NotEmpty(message = "'title' parameter shouldn't be empty.") String title,
            @Min(value = 1, message = "'pageNumber' parameter should be greater or equal to 1.") int pageNumber,
            @Min(value = 3, message = "'pageSize' parameter should be greater or equal to 3.") int pageSize){

        // declarations and instantiations
        Map<String, Object> response = new HashMap<>();  // the response that should be sent back to the client
        Page<Track> pageWithTracks;  // the page object with the tracks
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);  // instantiates the Pageable object

        // retrieve tracks in the given page
        pageWithTracks = trackRepository.findByTitleContaining(title, pageable);

        // mounts the response and return it
        response.put("items", pageWithTracks.getContent());
        response.put("totalItems", pageWithTracks.getTotalElements());
        response.put("totalPages", pageWithTracks.getTotalPages());

        return response;
    }
}
