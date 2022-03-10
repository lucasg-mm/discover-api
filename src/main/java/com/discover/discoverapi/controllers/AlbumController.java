package com.discover.discoverapi.controllers;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.services.AlbumService;
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
@RequestMapping(value = "/albums")
@AllArgsConstructor
public class AlbumController {
    private AlbumService albumService;

    //------ MAIN RESOURCE -------
    // get every stored album
    @Operation(summary = "Gets every stored album.")
    @GetMapping("")
    public ResponseEntity<List<Album>> findAll(){
        List<Album> allAlbums = albumService.findAll();
        return ResponseEntity.ok().body(allAlbums);
    }

    // get a specific album
    @Operation(summary = "Gets a specific album.")
    @GetMapping("/{id}")
    public ResponseEntity<Album> findById(@PathVariable long id){
        // retrieves the album
        Album foundAlbum = albumService.findById(id);
        return ResponseEntity.ok().body(foundAlbum);
    }

    // create a single album
    @Operation(summary = "Creates an album.")
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
    @Operation(summary = "Updates a specific album.")
    @PutMapping("/{id}")
    public ResponseEntity<Album> updateById(@PathVariable long id, @RequestBody Album albumDataToUpdate){
        // updates the album
        Album updatedAlbum = albumService.update(id,albumDataToUpdate);

        return ResponseEntity.ok().body(updatedAlbum);
    }

    // delete a single album
    @Operation(summary = "Deletes a specific album.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Album> deleteById(@PathVariable long id){
        // deletes the album
        albumService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ------ '/tracks' SUBRESOURCE -------
    // find all the tracks from the album
    @Operation(summary = "Returns every track from an album's list of tracks.")
    @GetMapping("{albumId}/tracks")
    public ResponseEntity<Set<Track>> findAllTracksOfAlbum(@PathVariable long albumId){
        Set<Track> allTracksOfAlbum = albumService.findAllTracksOfAlbum(albumId);

        return ResponseEntity.ok(allTracksOfAlbum);
    }

    // add an existing track to the album's tracks
    @Operation(summary = "Adds a track to an album's list of tracks.")
    @PutMapping("{albumId}/tracks/{trackId}")
    public ResponseEntity<Track> addTrackToAlbum(@PathVariable long albumId, @PathVariable long trackId){
        Track addedTrack = albumService.addTrackToAlbum(albumId, trackId);

        return ResponseEntity.ok(addedTrack);
    }

    // delete an existing track from a given album
    @Operation(summary = "Deletes a track from an album's list of tracks.")
    @DeleteMapping("{albumId}/tracks/{trackId}")
    public ResponseEntity<Track> deleteTrackFromAlbum(@PathVariable long albumId, @PathVariable long trackId){
        albumService.deleteTrackFromAlbum(albumId, trackId);

        return ResponseEntity.noContent().build();
    }

    // ------- '/cover' SUBRESOURCES --------
    // defines an album cover art
    @Operation(summary = "Defines an album's cover art by multipart/form-data upload.")
    @PutMapping("{albumId}/cover")
    public ResponseEntity<Album> setAlbumCover(@PathVariable long albumId, @RequestParam MultipartFile coverArt){
        albumService.setAlbumCover(albumId, coverArt);
        return ResponseEntity.ok().build();
    }

    // gets an album cover art
    @Operation(summary = "Returns an album's cover art as Content-type: image/png.")
    @GetMapping("{albumId}/cover")
    public ResponseEntity<ByteArrayResource> getAlbumCover(@PathVariable long albumId){
        byte[] imageData = albumService.getAlbumCover(albumId);
        ByteArrayResource resource = new ByteArrayResource(imageData);
        return ResponseEntity
                .ok()
                .header("Content-type", "image/png")
                .body(resource);
    }

    // --- '/search' SUBRESOURCES ---
    @Operation(summary = "Searches for albums by their titles.")
    @ApiResponse(responseCode = "200", ref ="#/components/responses/albumSearchResponse" )
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> findByTitleContaining(@RequestParam String title,
                                                       @RequestParam(defaultValue = "1") int pageNumber,
                                                       @RequestParam(defaultValue = "3") int pageSize){
        Map<String, Object> response = albumService.findByTitleContaining(title, pageNumber, pageSize);
        return ResponseEntity.ok().body(response);
    }
}
