package com.discover.discoverapi.controllers;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Artist;
import com.discover.discoverapi.entities.Genre;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.services.TrackService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/tracks")
@AllArgsConstructor
public class TrackController {
    private TrackService trackService;

    // get every stored track
    @GetMapping
    public ResponseEntity<List<Track>> findAll(){
        List<Track> allTracks = trackService.findAll();
        return ResponseEntity.ok(allTracks);
    }

    // get a specific track by id
    @GetMapping("/{id}")
    public ResponseEntity<Track> findById(@PathVariable long id){
        // retrieves the track and returns it
        Track foundTrack = trackService.findById(id);
        return ResponseEntity.ok(foundTrack);
    }

    // create a single track
    @PostMapping
    public ResponseEntity<Track> createOne(@RequestBody Track trackToCreate){
        // creates the new track
        Track createdTrack = trackService.create(trackToCreate);

        // gets the location of the created track
        URI createdTrackLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand()
                .toUri();

        // returns 201 created status code
        return ResponseEntity.created(createdTrackLocation).body(createdTrack);
    }

    // update a single track by id
    @PutMapping("/{id}")
    public ResponseEntity<Track> updateById(@PathVariable long id, @RequestBody Track trackDataToUpdate){
        // updates the track
        Track updatedTrack = trackService.update(id, trackDataToUpdate);

        // returns the updated track
        return ResponseEntity.ok(updatedTrack);
    }

    // delete a single track by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Track> deleteById(@PathVariable long id){
        // delete the track
        trackService.deleteById(id);

        // no content response
        return ResponseEntity.noContent().build();
    }
}
