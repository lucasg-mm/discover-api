package com.discover.discoverapi.controllers;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Artist;
import com.discover.discoverapi.entities.Genre;
import com.discover.discoverapi.entities.Track;
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
                .buildAndExpand(createdArtist.getId())
                .toUri();

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

    // ------- '/albums' SUBRESOURCE -------
    // find all the artists' albums
    @GetMapping("{artistId}/albums")
    public ResponseEntity<List<Album>> findAllAlbumsOfArtist(@PathVariable long artistId){
        // find the artist's albums
        List<Album> foundAlbums = artistService.findAllAlbumsOfArtist(artistId);
        return ResponseEntity.ok(foundAlbums);
    }

    // add an album to the artist's album
    @PutMapping("{artistId}/{albumId}")
    public ResponseEntity<Album> addAlbumToArtist(@PathVariable long artistId, @PathVariable long albumId){
        // add the album to the list of the artist's albums
        Album addedAlbum = artistService.addAlbumToArtist(artistId, albumId);
        return ResponseEntity.ok(addedAlbum);
    }

    // delete an existing album from a given artist
    @DeleteMapping("{artistId/albumId}")
    public ResponseEntity<Album> removeAlbumFromArtist(@PathVariable long artistId, @PathVariable long albumId){
        // remove album from the artist's list of albums
        artistService.deleteAlbumFromArtist(artistId, albumId);
        return ResponseEntity.noContent().build();
    }

    // ------- '/genres' SUBRESOURCE -------
    // find all the artists' genres
    @GetMapping("{artistId}/genres")
    public ResponseEntity<List<Genre>> findAllGenresOfArtist(@PathVariable long artistId){
        // find the artist's genres
        List<Genre> foundGenres = artistService.findAllGenresOfArtist(artistId);
        return ResponseEntity.ok(foundGenres);
    }

    // add a genre to the artist's genres
    @PutMapping("{artistId}/{genreId}")
    public ResponseEntity<Genre> addGenreToArtist(@PathVariable long artistId, @PathVariable long genreId){
        // add the genre to the list of the artist's genres
        Genre addedGenre = artistService.addGenreToArtist(artistId, genreId);
        return ResponseEntity.ok(addedGenre);
    }

    // delete an existing genre from a given artist
    @DeleteMapping("{artistId/genreId}")
    public ResponseEntity<Genre> removeGenreFromArtist(@PathVariable long artistId, @PathVariable long genreId){
        // remove genre from the artist's list of genres
        artistService.deleteGenreFromArtist(artistId, genreId);
        return ResponseEntity.noContent().build();
    }

    // ------- '/tracks' SUBRESOURCE -------
    // find all the artists' tracks
    @GetMapping("{artistId}/tracks}")
    public ResponseEntity<List<Track>> findAllTracksOfArtist(@PathVariable long artistId){
        // find the artist's tracks
        List<Track> foundTracks = artistService.findAllTracksOfArtist(artistId);
        return ResponseEntity.ok(foundTracks);
    }

    // add a track to the artist's tracks
    @PutMapping("{artistId}/{trackId}")
    public ResponseEntity<Track> addTrackToArtist(@PathVariable long artistId, @PathVariable long trackId){
        // add the track to the list of the artist's tracks
        Track addedTrack = artistService.addTrackToArtist(artistId, trackId);
        return ResponseEntity.ok(addedTrack);
    }

    // delete an existing track from a given artist
    @DeleteMapping("{artistId/trackId}")
    public ResponseEntity<Track> removeTrackFromArtist(@PathVariable long artistId, @PathVariable long trackId){
        // remove track from the artist's list of tracks
        artistService.deleteTrackFromArtist(artistId, trackId);
        return ResponseEntity.noContent().build();
    }
}
