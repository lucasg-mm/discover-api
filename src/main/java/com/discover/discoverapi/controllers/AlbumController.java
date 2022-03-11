package com.discover.discoverapi.controllers;

import com.discover.discoverapi.controllers.exceptions.StandardError;
import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.services.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(description = "Gets every stored album.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<List<Album>> findAll(){
        List<Album> allAlbums = albumService.findAll();
        return ResponseEntity.ok().body(allAlbums);
    }

    // get a specific album
    @Operation(description = "Gets a specific album.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Album> findById(@PathVariable long id){
        // retrieves the album
        Album foundAlbum = albumService.findById(id);
        return ResponseEntity.ok().body(foundAlbum);
    }

    // create a single album
    @Operation(description = "Creates an album.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @PostMapping(value = "", produces = "application/json")
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
    @Operation(description = "Updates a specific album.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Album> updateById(@PathVariable long id, @RequestBody Album albumDataToUpdate){
        // updates the album
        Album updatedAlbum = albumService.update(id,albumDataToUpdate);

        return ResponseEntity.ok().body(updatedAlbum);
    }

    // delete a single album
    @Operation(description = "Deletes a specific album.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = @Content),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Album> deleteById(@PathVariable long id){
        // deletes the album
        albumService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ------ '/tracks' SUBRESOURCE -------
    // find all the tracks from the album
    @Operation(description = "Returns every track from an album's list of tracks.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping(value = "{albumId}/tracks", produces = "application/json")
    public ResponseEntity<Set<Track>> findAllTracksOfAlbum(@PathVariable long albumId){
        Set<Track> allTracksOfAlbum = albumService.findAllTracksOfAlbum(albumId);

        return ResponseEntity.ok(allTracksOfAlbum);
    }

    // add an existing track to the album's tracks
    @Operation(description = "Adds a track to an album's list of tracks.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @PutMapping(value = "{albumId}/tracks/{trackId}", produces = "application/json")
    public ResponseEntity<Track> addTrackToAlbum(@PathVariable long albumId, @PathVariable long trackId){
        Track addedTrack = albumService.addTrackToAlbum(albumId, trackId);

        return ResponseEntity.ok(addedTrack);
    }

    // delete an existing track from a given album
    @Operation(description = "Deletes a track from an album's list of tracks.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = @Content),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @DeleteMapping(value = "{albumId}/tracks/{trackId}", produces = "application/json")
    public ResponseEntity<Track> deleteTrackFromAlbum(@PathVariable long albumId, @PathVariable long trackId){
        albumService.deleteTrackFromAlbum(albumId, trackId);

        return ResponseEntity.noContent().build();
    }

    // ------- '/cover' SUBRESOURCES --------
    // defines an album cover art
    @Operation(description = "Defines an album's cover art by multipart/form-data upload.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @PutMapping(value = "{albumId}/cover", produces = "application/json")
    public ResponseEntity<Album> setAlbumCover(@PathVariable long albumId, @RequestParam MultipartFile coverArt){
        albumService.setAlbumCover(albumId, coverArt);
        return ResponseEntity.ok().build();
    }

    // gets an album cover art
    @Operation(description = "Returns an album's cover art as Content-type: image/png.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping(value = "{albumId}/cover", produces = "image/png")
    public ResponseEntity<ByteArrayResource> getAlbumCover(@PathVariable long albumId){
        byte[] imageData = albumService.getAlbumCover(albumId);
        ByteArrayResource resource = new ByteArrayResource(imageData);
        return ResponseEntity
                .ok()
                .header("Content-type", "image/png")
                .body(resource);
    }

    // --- '/search' SUBRESOURCES ---
    @Operation(description = "Searches for albums by their titles.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    ref ="#/components/responses/albumSearchResponse" ),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity<Map<String, Object>> findByTitleContaining(@RequestParam String title,
                                                       @RequestParam(defaultValue = "1") int pageNumber,
                                                       @RequestParam(defaultValue = "3") int pageSize){
        Map<String, Object> response = albumService.findByTitleContaining(title, pageNumber, pageSize);
        return ResponseEntity.ok().body(response);
    }
}
