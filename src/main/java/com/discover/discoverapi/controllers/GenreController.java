package com.discover.discoverapi.controllers;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Artist;
import com.discover.discoverapi.entities.Genre;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.services.GenreService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/genres")
@AllArgsConstructor
public class GenreController {
    private GenreService genreService;

    // get every stored genre
    @GetMapping("")
    public ResponseEntity<List<Genre>> findAll(){
        List<Genre> allGenres = genreService.findAll();
        return ResponseEntity.ok(allGenres);
    }

    // get a specific genre by id
    @GetMapping("/{id}")
    public ResponseEntity<Genre> findById(@PathVariable long id){
        // retrieves the genre and returns it
        Genre foundGenre = genreService.findById(id);
        return  ResponseEntity.ok(foundGenre);
    }

    // create a single genre
    @PostMapping("")
    public ResponseEntity<Genre> createOne(@RequestBody Genre genreToCreate){
        // creates the new genre
        Genre createdGenre = genreService.create(genreToCreate);

        // gets the location of the newly created genre
        URI createdGenreLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(createdGenre.getId())
                .toUri();

        // returns 201 created status code
        return ResponseEntity.created(createdGenreLocation).body(createdGenre);
    }

    // update a single genre by id
    @PutMapping("/{id}")
    public ResponseEntity<Genre> updateById(@PathVariable long id, @RequestBody Genre genreDataToUpdate){
        // updates the genre
        Genre updatedGenre = genreService.update(id, genreDataToUpdate);

        // returns the updated genre
        return ResponseEntity.ok(updatedGenre);
    }

    // delete a single genre by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Genre> deleteById(@PathVariable long id){
        // deletes the genre
        genreService.deleteById(id);

        // no content response
        return ResponseEntity.noContent().build();
    }

    // ------- '/albums' SUBRESOURCE -------
    // find all the genres' albums
    @GetMapping("{genreId}/{albumId}")
    public ResponseEntity<Album> findAllAlbumsOfGenre(@PathVariable long genreId, @PathVariable long albumId){
        // find the genre's albums
        Album foundAlbums = genreService.findAllAlbumsOfGenre(genreId, albumId);
        return ResponseEntity.ok(foundAlbums);
    }

    // add an album to the genre's album
    @PutMapping("{genreId}/{albumId}")
    public ResponseEntity<Album> addAlbumToGenre(@PathVariable long genreId, @PathVariable long albumId){
        // add the album to the list of the genre's albums
        Album addedAlbum = genreService.addAlbumToGenre(genreId, albumId);
        return ResponseEntity.ok(addedAlbum);
    }

    // delete an existing album from a given genre
    @DeleteMapping("{genreId/albumId}")
    public ResponseEntity<Album> removeAlbumFromGenre(@PathVariable long genreId, @PathVariable long albumId){
        // remove album from the genre's list of albums
        genreService.deleteAlbumFromGenre(genreId, albumId);
        return ResponseEntity.noContent().build();
    }

    // ------- '/artists' SUBRESOURCE -------
    // find all the genres' artists
    @GetMapping("{genreId}/{artistId}")
    public ResponseEntity<Artist> findAllArtistsOfGenre(@PathVariable long genreId, @PathVariable long artistId){
        // find the genre's artists
        Artist foundArtists = genreService.findAllArtistsOfGenre(genreId, artistId);
        return ResponseEntity.ok(foundArtists);
    }

    // add an artist to the genre's artist
    @PutMapping("{genreId}/{artistId}")
    public ResponseEntity<Artist> addArtistToGenre(@PathVariable long genreId, @PathVariable long artistId){
        // add the artist to the list of the genre's artists
        Artist addedArtist = genreService.addArtistToGenre(genreId, artistId);
        return ResponseEntity.ok(addedArtist);
    }

    // delete an existing artist from a given genre
    @DeleteMapping("{genreId/artistId}")
    public ResponseEntity<Artist> removeArtistFromGenre(@PathVariable long genreId, @PathVariable long artistId){
        // remove artist from the genre's list of artists
        genreService.deleteArtistFromGenre(genreId, artistId);
        return ResponseEntity.noContent().build();
    }

    // ------- '/tracks' SUBRESOURCE -------
    // find all the genres' tracks
    @GetMapping("{genreId}/{trackId}")
    public ResponseEntity<Track> findAllTracksOfGenre(@PathVariable long genreId, @PathVariable long trackId){
        // find the genre's tracks
        Track foundTracks = genreService.findAllTracksOfGenre(genreId, trackId);
        return ResponseEntity.ok(foundTracks);
    }

    // add an track to the genre's track
    @PutMapping("{genreId}/{trackId}")
    public ResponseEntity<Track> addTrackToGenre(@PathVariable long genreId, @PathVariable long trackId){
        // add the track to the list of the genre's tracks
        Track addedTrack = genreService.addTrackToGenre(genreId, trackId);
        return ResponseEntity.ok(addedTrack);
    }

    // delete an existing track from a given genre
    @DeleteMapping("{genreId/trackId}")
    public ResponseEntity<Track> removeTrackFromGenre(@PathVariable long genreId, @PathVariable long trackId){
        // remove track from the genre's list of tracks
        genreService.deleteTrackFromGenre(genreId, trackId);
        return ResponseEntity.noContent().build();
    }
}
