package com.discover.discoverapi.controllers;

import com.discover.discoverapi.entities.Artist;
import com.discover.discoverapi.services.ArtistService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/artists")
@AllArgsConstructor
public class ArtistController {
    private ArtistService artistService;

    // get every stored artist
    @GetMapping("")
    public ResponseEntity<List<Artist>> findAll(){
        List<Artist> allArtists = artistService.findAll();
        return ResponseEntity.ok().body(allArtists);
    }

    // get a specific artist
    @GetMapping("/{id}")
    public ResponseEntity<Artist> findById(@PathVariable long id){
        // retrieves the artist and returns it
        Artist foundArtist = artistService.findById(id);
        return ResponseEntity.ok(foundArtist);
    }

    // create a single artist
    @PostMapping("")
    public ResponseEntity<Artist> createOne(@RequestBody Artist artistToCreate){
        // creates the new artist
        Artist createdArtist = artistService.create(artistToCreate);

        // gets the location of the newly created artist
        URI createdArtistLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(createdArtist.getId()).toUri();

        // returns with 201 created status code
        return ResponseEntity.created(createdArtistLocation).body(createdArtist);
    }

    // update a single artist by id
    @PutMapping("/{id}")
    public ResponseEntity<Artist> updateById(@PathVariable long id, @RequestBody Artist artistDataToUpdate){
        // updates the artist
        Artist updatedArtist = artistService.update(id, artistDataToUpdate);

        // returns the updated artist
        return ResponseEntity.ok(updatedArtist);
    }

    // delete a single artist by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Artist> deleteById(@PathVariable long id){
        // deletes the artist
        artistService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
