package com.discover.discoverapi.controllers;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Artist;
import com.discover.discoverapi.entities.Genre;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.services.AlbumService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/albums")
@AllArgsConstructor
public class AlbumController {
    private AlbumService albumService;

    //------ MAIN RESOURCE -------
    // get every stored album
    @GetMapping("")
    public ResponseEntity<List<Album>> findAll(){
        List<Album> allAlbums = albumService.findAll();
        return ResponseEntity.ok().body(allAlbums);
    }

    // get a specific album
    @GetMapping("/{id}")
    public ResponseEntity<Album> findById(@PathVariable long id){
        // retrieves the album
        Album foundAlbum = albumService.findById(id);
        return ResponseEntity.ok().body(foundAlbum);
    }

    // create a single album
    @PostMapping("")
    public ResponseEntity<Album> createOne(@RequestBody Album albumToCreate){
        // persists the new album
        Album createdAlbum = albumService.create(albumToCreate);

        // gets the location of the new album
        URI createdAlbumLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdAlbum.getId())
                .toUri();

        return ResponseEntity.created(createdAlbumLocation).body(createdAlbum);
    }

    // update a single album
    @PutMapping("/{id}")
    public ResponseEntity<Album> updateById(@PathVariable long id, @RequestBody Album albumDataToUpdate){
        // updates the album
        Album updatedAlbum = albumService.update(id,albumDataToUpdate);

        return ResponseEntity.ok().body(updatedAlbum);
    }

    // delete a single album
    @DeleteMapping("/{id}")
    public ResponseEntity<Album> deleteById(@PathVariable long id){
        // deletes the album
        albumService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ------ '/artists' SUBRESOURCE -------
    // find all the main artists that recorded the album
    @GetMapping("{albumId}/artists")
    public ResponseEntity<List<Artist>> findAllArtistsOfAlbum(@PathVariable long albumId){
        List<Artist> allArtistsOfAlbum = albumService.findAllArtistsOfAlbum(albumId);

        return ResponseEntity.ok(allArtistsOfAlbum);
    }

    // add an existing artist to the album's artists
    @PutMapping("{albumId}/{artistId}")
    public ResponseEntity<Artist> addArtistToAlbum(@PathVariable long albumId, @PathVariable long artistId){
        Artist addedArtist = albumService.addArtistToAlbum(albumId, artistId);

        return ResponseEntity.ok(addedArtist);
    }

    // delete an existing artist from a given album
    @DeleteMapping("{albumId}/{artistId}")
    public ResponseEntity<Artist> deleteArtistFromAlbum(@PathVariable long albumId, @PathVariable long artistId){
        albumService.deleteArtistFromAlbum(albumId, artistId);

        return ResponseEntity.noContent().build();
    }

    // ------ '/genres' SUBRESOURCES -------
    // find all the main genres that recorded the album
    @GetMapping("{albumId}/genres")
    public ResponseEntity<List<Genre>> findAllGenresOfAlbum(@PathVariable long albumId){
        List<Genre> allGenresOfAlbum = albumService.findAllGenresOfAlbum(albumId);

        return ResponseEntity.ok(allGenresOfAlbum);
    }

    // add an existing genre to the album's genres
    @PutMapping("{albumId}/{genreId}")
    public ResponseEntity<Genre> addGenreToAlbum(@PathVariable long albumId, @PathVariable long genreId){
        Genre addedGenre = albumService.addGenreToAlbum(albumId, genreId);

        return ResponseEntity.ok(addedGenre);
    }

    // delete an existing genre from a given album
    @DeleteMapping("{albumId}/{genreId}")
    public ResponseEntity<Genre> deleteGenreFromAlbum(@PathVariable long albumId, @PathVariable long genreId){
        albumService.deleteGenreFromAlbum(albumId, genreId);

        return ResponseEntity.noContent().build();
    }

    // ------ '/tracks' SUBRESOURCES -------
    // find all the main tracks that recorded the album
    @GetMapping("{albumId}/tracks")
    public ResponseEntity<List<Track>> findAllTracksOfAlbum(@PathVariable long albumId){
        List<Track> allTracksOfAlbum = albumService.findAllTracksOfAlbum(albumId);

        return ResponseEntity.ok(allTracksOfAlbum);
    }

    // add an existing track to the album's tracks
    @PutMapping("{albumId}/{trackId}")
    public ResponseEntity<Track> addTrackToAlbum(@PathVariable long albumId, @PathVariable long trackId){
        Track addedTrack = albumService.addTrackToAlbum(albumId, trackId);

        return ResponseEntity.ok(addedTrack);
    }

    // delete an existing track from a given album
    @DeleteMapping("{albumId}/{trackId}")
    public ResponseEntity<Track> deleteTrackFromAlbum(@PathVariable long albumId, @PathVariable long trackId){
        albumService.deleteTrackFromAlbum(albumId, trackId);

        return ResponseEntity.noContent().build();
    }
}
