package com.discover.discoverapi.controllers;

import com.discover.discoverapi.controllers.exceptions.StandardError;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.services.TrackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/tracks")
@AllArgsConstructor
@Tag(name = "Track Controller")
public class TrackController {
    private TrackService trackService;

    //------ MAIN RESOURCE -------
    // get every stored track (paginated)
    @Operation(description = "Gets every stored track in a paginated way.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    ref = "#/components/responses/trackPaginatedResponse"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<Map<String, Object>> findAll(
            @Parameter(description = "The number of the page that should be retrieved (starting with 1).")
            @RequestParam(defaultValue = "1") int pageNumber,
            @Parameter(description = "Number of items in each page.")
            @RequestParam(defaultValue = "3") int pageSize
    ){
        Map<String, Object> paginatedTracks = trackService.findAll(pageNumber, pageSize);
        return ResponseEntity.ok().body(paginatedTracks);
    }

    // get a specific track by id
    @Operation(description = "Returns a specific track.")
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
    public ResponseEntity<Track> findById(
            @Parameter(description="Id from the track that should be retrieved.") @PathVariable long id){
        // retrieves the track and returns it
        Track foundTrack = trackService.findById(id);
        return ResponseEntity.ok(foundTrack);
    }

    // create a single track
    @Operation(description = "Creates a track.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @PostMapping(produces = "application/json")
    public ResponseEntity<Track> createOne(@RequestBody Track trackToCreate){
        // creates the new track
        Track createdTrack = trackService.create(trackToCreate);

        // gets the location of the created track
        URI createdTrackLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTrack.getId())
                .toUri();

        // returns 201 created status code
        return ResponseEntity.created(createdTrackLocation).body(createdTrack);
    }

    // update a single track by id
    @Operation(description = "Updates a specific track's title and length.")
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
    public ResponseEntity<Track> updateById(
            @Parameter(description="Id from the track that should be updated") @PathVariable long id,
            @RequestBody Track trackDataToUpdate){
        // updates the track
        Track updatedTrack = trackService.update(id, trackDataToUpdate);

        // returns the updated track
        return ResponseEntity.ok(updatedTrack);
    }

    // delete a single track by id
    @Operation(description = "Deletes a specific track.")
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
    public ResponseEntity<Track> deleteById(
            @Parameter(description="Id from the track that should be deleted.") @PathVariable long id){
        // delete the track
        trackService.deleteById(id);

        // no content response
        return ResponseEntity.noContent().build();
    }

    // --- '/search' SUBRESOURCES ---
    @Operation(description = "Searches for a track by its title.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    ref ="#/components/responses/trackSearchResponse"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity<Map<String, Object>> findByTitleContaining(
            @Parameter(description="The track's title that should be searched.") @RequestParam String title,
            @Parameter(description="The number of the page that should be retrieved (starting with 1).") @RequestParam(defaultValue = "1") int pageNumber,
            @Parameter(description="Number of items in each page.") @RequestParam(defaultValue = "3") int pageSize){
        Map<String, Object> response = trackService.findByTitleContaining(title, pageNumber, pageSize);
        return ResponseEntity.ok().body(response);
    }
}
