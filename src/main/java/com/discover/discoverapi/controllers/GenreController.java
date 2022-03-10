package com.discover.discoverapi.controllers;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Artist;
import com.discover.discoverapi.entities.Genre;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.services.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/genres")
@AllArgsConstructor
public class GenreController {
    private GenreService genreService;

    // get every stored genre
    @Operation(summary = "Gets all stored genres.")
    @GetMapping("")
    public ResponseEntity<List<Genre>> findAll(){
        List<Genre> allGenres = genreService.findAll();
        return ResponseEntity.ok(allGenres);
    }

    // get a specific genre by id
    @Operation(summary = "Gets a specific Genre")
    @GetMapping("/{id}")
    public ResponseEntity<Genre> findById(@PathVariable long id){
        // retrieves the genre and returns it
        Genre foundGenre = genreService.findById(id);
        return  ResponseEntity.ok(foundGenre);
    }

    // create a single genre
    @Operation(summary = "Creates a specific Genre")
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
    @Operation(summary = "Updates a specific Genre")
    @PutMapping("/{id}")
    public ResponseEntity<Genre> updateById(@PathVariable long id, @RequestBody Genre genreDataToUpdate){
        // updates the genre
        Genre updatedGenre = genreService.update(id, genreDataToUpdate);

        // returns the updated genre
        return ResponseEntity.ok(updatedGenre);
    }

    // delete a single genre by id
    @Operation(summary = "Deletes a specific genre.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Genre> deleteById(@PathVariable long id){
        // deletes the genre
        genreService.deleteById(id);

        // no content response
        return ResponseEntity.noContent().build();
    }

    // ------- '/albums' SUBRESOURCE -------
    // find all the genres' albums
    @Operation(summary = "Gets all albums from a genre's list of albums.")
    @GetMapping("{genreId}/albums")
    public ResponseEntity<Set<Album>> findAllAlbumsOfGenre(@PathVariable long genreId){
        // find the genre's albums
        Set<Album> foundAlbums = genreService.findAllAlbumsOfGenre(genreId);
        return ResponseEntity.ok(foundAlbums);
    }

    // add an album to the genre's album
    @Operation(summary = "Adds an album to a genre's list of albums.")
    @PutMapping("{genreId}/genres/{albumId}")
    public ResponseEntity<Album> addAlbumToGenre(@PathVariable long genreId, @PathVariable long albumId){
        // add the album to the list of the genre's albums
        Album addedAlbum = genreService.addAlbumToGenre(genreId, albumId);
        return ResponseEntity.ok(addedAlbum);
    }

    // delete an existing album from a given genre
    @Operation(summary = "Deletes an album from a genre's list of albums.")
    @DeleteMapping("{genreId}/genres/{albumId}")
    public ResponseEntity<Album> removeAlbumFromGenre(@PathVariable long genreId, @PathVariable long albumId){
        // remove album from the genre's list of albums
        genreService.deleteAlbumFromGenre(genreId, albumId);
        return ResponseEntity.noContent().build();
    }

    // ------- '/artists' SUBRESOURCE -------
    // find all the genres' artists
    @Operation(summary = "Gets all artists from a genre's list of artists.")
    @GetMapping("{genreId}/artists")
    public ResponseEntity<Set<Artist>> findAllArtistsOfGenre(@PathVariable long genreId){
        // find the genre's artists
        Set<Artist> foundArtists = genreService.findAllArtistsOfGenre(genreId);
        return ResponseEntity.ok(foundArtists);
    }

    // add an artist to the genre's artist
    @Operation(summary = "Adds an artist to a genre's list of artists.")
    @PutMapping("{genreId}/artists/{artistId}")
    public ResponseEntity<Artist> addArtistToGenre(@PathVariable long genreId, @PathVariable long artistId){
        // add the artist to the list of the genre's artists
        Artist addedArtist = genreService.addArtistToGenre(genreId, artistId);
        return ResponseEntity.ok(addedArtist);
    }

    // delete an existing artist from a given genre
    @Operation(summary = "Deletes an artist from a genre's list of artists.")
    @DeleteMapping("{genreId}/artists/{artistId}")
    public ResponseEntity<Artist> removeArtistFromGenre(@PathVariable long genreId, @PathVariable long artistId){
        // remove artist from the genre's list of artists
        genreService.deleteArtistFromGenre(genreId, artistId);
        return ResponseEntity.noContent().build();
    }

    // ------- '/tracks' SUBRESOURCE -------
    // find all the genres' tracks
    @Operation(summary = "Gets all tracks from a genre's list of tracks.")
    @GetMapping("{genreId}/tracks")
    public ResponseEntity<Set<Track>> findAllTracksOfGenre(@PathVariable long genreId){
        // find the genre's tracks
        Set<Track> foundTracks = genreService.findAllTracksOfGenre(genreId);
        return ResponseEntity.ok(foundTracks);
    }

    // add a track to the genre's track
    @Operation(summary = "Adds a track to a genre's list of tracks.")
    @PutMapping("{genreId}/tracks/{trackId}")
    public ResponseEntity<Track> addTrackToGenre(@PathVariable long genreId, @PathVariable long trackId){
        // add the track to the list of the genre's tracks
        Track addedTrack = genreService.addTrackToGenre(genreId, trackId);
        return ResponseEntity.ok(addedTrack);
    }

    // delete an existing track from a given genre
    @Operation(summary = "Deletes a track from a genre's list of tracks.")
    @DeleteMapping("{genreId}/tracks/{trackId}")
    public ResponseEntity<Track> removeTrackFromGenre(@PathVariable long genreId, @PathVariable long trackId){
        // remove track from the genre's list of tracks
        genreService.deleteTrackFromGenre(genreId, trackId);
        return ResponseEntity.noContent().build();
    }

    // --- '/search' SUBRESOURCES ---
    @Operation(summary = "Searches for genres by their names.")
    @ApiResponse(responseCode = "200", ref ="#/components/responses/genreSearchResponse" )
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> findByNameContaining(@RequestParam String name,
                                                                    @RequestParam(defaultValue = "1") int pageNumber,
                                                                    @RequestParam(defaultValue = "3") int pageSize){
        Map<String, Object> response = genreService.findByNameContaining(name, pageNumber, pageSize);
        return ResponseEntity.ok().body(response);
    }
}
