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
                .path("{id}")
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

    // ------- '/album' SUBRESOURCE --------
    // find the track's album
    @GetMapping("{trackId}/album")
    public ResponseEntity<Album> findAlbumOfTheTrack(@PathVariable long trackId){
        Album albumOfTheTrack = trackService.findAlbumOfTheTrack(trackId);
        return ResponseEntity.ok(albumOfTheTrack);
    }

    // assign an album as the track's album
    @PutMapping("{trackId}/{albumId}")
    public ResponseEntity<Album> assignAlbumToTrack(@PathVariable long trackId, @PathVariable long albumId){
        Album assignedAlbum = trackService.assignAlbumToTrack(trackId, albumId);
        return ResponseEntity.ok(assignedAlbum);
    }

    // unassign the track's album
    @DeleteMapping("{trackId}/album")
    public ResponseEntity<Album> unassignAlbumFromTrack(@PathVariable long trackId){
        trackService.unasassignAlbumFromTrack(trackId);
        return ResponseEntity.noContent().build();
    }

    // ------- '/genres' SUBRESOURCE -------
    // find all the tracks' genres
    @GetMapping("{trackId}/{genreId}")
    public ResponseEntity<Genre> findAllGenresOfTrack(@PathVariable long trackId, @PathVariable long genreId){
        // find the track's genres
        Genre foundGenres = trackService.findAllGenresOfTrack(trackId, genreId);
        return ResponseEntity.ok(foundGenres);
    }

    // add a genre to the track's genres
    @PutMapping("{trackId}/{genreId}")
    public ResponseEntity<Genre> addGenreToTrack(@PathVariable long trackId, @PathVariable long genreId){
        // add the genre to the list of the track's genres
        Genre addedGenre = trackService.addGenreToTrack(trackId, genreId);
        return ResponseEntity.ok(addedGenre);
    }

    // delete an existing genre from a given track
    @DeleteMapping("{trackId/genreId}")
    public ResponseEntity<Genre> removeGenreFromTrack(@PathVariable long trackId, @PathVariable long genreId) {
        // remove genre from the track's list of genres
        trackService.deleteGenreFromTrack(trackId, genreId);
        return ResponseEntity.noContent().build();
    }


    // ------- '/artists' SUBRESOURCE -------
    // find all the tracks' artists
    @GetMapping("{trackId}/{artistId}")
    public ResponseEntity<Artist> findAllArtistsOfTrack(@PathVariable long trackId, @PathVariable long artistId){
        // find the track's artists
        Artist foundArtists = trackService.findAllArtistsOfTrack(trackId, artistId);
        return ResponseEntity.ok(foundArtists);
    }

    // add an artist to the track's artist
    @PutMapping("{trackId}/{artistId}")
    public ResponseEntity<Artist> addArtistToTrack(@PathVariable long trackId, @PathVariable long artistId){
        // add the artist to the list of the track's artists
        Artist addedArtist = trackService.addArtistToTrack(trackId, artistId);
        return ResponseEntity.ok(addedArtist);
    }

    // delete an existing artist from a given track
    @DeleteMapping("{trackId/artistId}")
    public ResponseEntity<Artist> removeArtistFromTrack(@PathVariable long trackId, @PathVariable long artistId) {
        // remove artist from the track's list of artists
        trackService.deleteArtistFromTrack(trackId, artistId);
        return ResponseEntity.noContent().build();
    }
}
