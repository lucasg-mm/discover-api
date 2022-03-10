package com.discover.discoverapi.controllers;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Artist;
import com.discover.discoverapi.entities.Genre;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.services.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/artists")
@AllArgsConstructor
public class ArtistController {
    private ArtistService artistService;

    // get every stored artist
    @Operation(summary = "Returns all artists.")
    @GetMapping("")
    public ResponseEntity<List<Artist>> findAll(){
        List<Artist> allArtists = artistService.findAll();
        return ResponseEntity.ok().body(allArtists);
    }

    // get a specific artist
    @Operation(summary = "Returns a specific artist.")
    @GetMapping("/{id}")
    public ResponseEntity<Artist> findById(@PathVariable long id){
        // retrieves the artist and returns it
        Artist foundArtist = artistService.findById(id);
        return ResponseEntity.ok(foundArtist);
    }

    // create a single artist
    @Operation(summary = "Creates an artist.")
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
    @Operation(summary = "Updates a specific artist's name")
    @PutMapping("/{id}")
    public ResponseEntity<Artist> updateById(@PathVariable long id, @RequestBody Artist artistDataToUpdate){
        // updates the artist
        Artist updatedArtist = artistService.update(id, artistDataToUpdate);

        // returns the updated artist
        return ResponseEntity.ok(updatedArtist);
    }

    // delete a single artist by id
    @Operation(summary = "Deletes a specific artist.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Artist> deleteById(@PathVariable long id){
        // deletes the artist
        artistService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ------- '/albums' SUBRESOURCE -------
    // find all the artists' albums
    @Operation(summary = "Returns all albums from an artist")
    @GetMapping("{artistId}/albums")
    public ResponseEntity<Set<Album>> findAllAlbumsOfArtist(@PathVariable long artistId){
        // find the artist's albums
        Set<Album> foundAlbums = artistService.findAllAlbumsOfArtist(artistId);
        return ResponseEntity.ok(foundAlbums);
    }

    // add an album to the artist's album
    @Operation(summary = "Adds an album to an artist's list of albums.")
    @PutMapping("{artistId}/albums/{albumId}")
    public ResponseEntity<Album> addAlbumToArtist(@PathVariable long artistId, @PathVariable long albumId){
        // add the album to the list of the artist's albums
        Album addedAlbum = artistService.addAlbumToArtist(artistId, albumId);
        return ResponseEntity.ok(addedAlbum);
    }

    // delete an existing album from a given artist
    @Operation(summary = "Deletes an album from an artist's list of albums.")
    @DeleteMapping("{artistId}/albums/{albumId}")
    public ResponseEntity<Album> removeAlbumFromArtist(@PathVariable long artistId, @PathVariable long albumId){
        // remove album from the artist's list of albums
        artistService.deleteAlbumFromArtist(artistId, albumId);
        return ResponseEntity.noContent().build();
    }

    // ------- '/tracks' SUBRESOURCE -------
    // find all the artists' tracks
    @Operation(summary = "Returns all tracks from a artist.")
    @GetMapping("{artistId}/tracks")
    public ResponseEntity<Set<Track>> findAllTracksOfArtist(@PathVariable long artistId){
        // find the artist's tracks
        Set<Track> foundTracks = artistService.findAllTracksOfArtist(artistId);
        return ResponseEntity.ok(foundTracks);
    }

    // add a track to the artist's tracks
    @Operation(summary = "Adds a track to the artist's list of tracks.")
    @PutMapping("{artistId}/tracks/{trackId}")
    public ResponseEntity<Track> addTrackToArtist(@PathVariable long artistId, @PathVariable long trackId){
        // add the track to the list of the artist's tracks
        Track addedTrack = artistService.addTrackToArtist(artistId, trackId);
        return ResponseEntity.ok(addedTrack);
    }

    // delete an existing track from a given artist
    @Operation(summary = "Deletes a track from the artist's list of tracks.")
    @DeleteMapping("{artistId}/tracks/{trackId}")
    public ResponseEntity<Track> removeTrackFromArtist(@PathVariable long artistId, @PathVariable long trackId){
        // remove track from the artist's list of tracks
        artistService.deleteTrackFromArtist(artistId, trackId);
        return ResponseEntity.noContent().build();
    }

    // ------- '/image' SUBRESOURCE --------

    // defines an artist's image
    @Operation(summary = "Defines an artist's profile image by multipart/form-data upload.")
    @PutMapping("{artistId}/image")
    public ResponseEntity<Artist> setImage(@PathVariable long artistId, @RequestParam MultipartFile image){
        artistService.setArtistImage(artistId, image);
        return ResponseEntity.ok().build();
    }

    // gets an artist's image
    @Operation(summary = "Gets an artist's profile image.")
    @GetMapping("{artistId}/image")
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable long artistId){
        byte[] imageData = artistService.getArtistImage(artistId);
        ByteArrayResource resource = new ByteArrayResource(imageData);
        return ResponseEntity
                .ok()
                .header("Content-type", "image/png")
                .body(resource);
    }

    // --- '/search' SUBRESOURCES ---
    @Operation(summary = "Searches for an artist by their name.")
    @ApiResponse(responseCode = "200", ref ="#/components/responses/artistSearchResponse" )
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> findByNameContaining(@RequestParam String name,
                                                                     @RequestParam(defaultValue = "1") int pageNumber,
                                                                     @RequestParam(defaultValue = "3") int pageSize){
        Map<String, Object> response = artistService.findByNameContaining(name, pageNumber, pageSize);
        return ResponseEntity.ok().body(response);
    }
}
