package com.discover.discoverapi.controllers;

import com.discover.discoverapi.controllers.exceptions.StandardError;
import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.services.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Set;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/albums")
@AllArgsConstructor
@Tag(name = "Album Controller")
public class AlbumController {
    private AlbumService albumService;

    //------ MAIN RESOURCE -------
    // get every stored album (paginated)
    @Operation(description = "Gets every stored album in a paginated way.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    ref = "#/components/responses/albumPaginatedResponse"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<Map<String, Object>> findAll(
            @Parameter(description = "The number of the page that should be retrieved (starting with 1).")
            @RequestParam(defaultValue = "1") int pageNumber,
            @Parameter(description = "Number of items in each page.")
            @RequestParam(defaultValue = "3") int pageSize
    ) {
        Map<String, Object> paginatedAlbums = albumService.findAll(pageNumber, pageSize);
        return ResponseEntity.ok().body(paginatedAlbums);
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
    public ResponseEntity<Album> findById(
            @Parameter(description = "Id of the album to be retrieved.") @PathVariable long id) {
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
    public ResponseEntity<Album> createOne(@RequestBody Album albumToCreate) {
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
    public ResponseEntity<Album> updateById(
            @Parameter(description = "Id of the album to be updated.") @PathVariable long id,
            @RequestBody Album albumDataToUpdate) {
        // updates the album
        Album updatedAlbum = albumService.update(id, albumDataToUpdate);

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
    public ResponseEntity<Album> deleteById(
            @Parameter(description = "Id of the album to be deleted.") @PathVariable long id) {
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
    public ResponseEntity<Set<Track>> findAllTracksOfAlbum(
            @Parameter(description = "Id of the album the tracks should be from.") @PathVariable long albumId) {
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
    public ResponseEntity<Track> addTrackToAlbum(
            @Parameter(description = "Id of the album the track should be from.") @PathVariable long albumId,
            @Parameter(description = "Id of the track that should be added") @PathVariable long trackId) {
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
    public ResponseEntity<Track> deleteTrackFromAlbum(
            @Parameter(description = "Id of the album the track is from.") @PathVariable long albumId,
            @Parameter(description = "Id of the track that should be removed from the album's list of tracks.") @PathVariable long trackId) {
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
    public ResponseEntity<Album> setAlbumCover(
            @Parameter(description = "Id of the album that the image is being uploaded to.") @PathVariable long albumId,
            @RequestParam MultipartFile coverArt) {
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
    public ResponseEntity<ByteArrayResource> getAlbumCover(
            @Parameter(description = "Id of the album that the cover is from.") @PathVariable long albumId) {
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
                    ref = "#/components/responses/albumPaginatedResponse"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity<Map<String, Object>> findByTitleContaining(
            @Parameter(description = "The album's title that should be searched.")
            @RequestParam String title,
            @Parameter(description = "The number of the page that should be retrieved (starting with 1).")
            @RequestParam(defaultValue = "1") int pageNumber,
            @Parameter(description = "Number of items in each page.")
            @RequestParam(defaultValue = "3") int pageSize) {
        Map<String, Object> response = albumService.findByTitleContaining(title, pageNumber, pageSize);
        return ResponseEntity.ok().body(response);
    }
}
