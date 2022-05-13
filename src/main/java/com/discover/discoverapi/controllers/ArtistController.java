package com.discover.discoverapi.controllers;

import com.discover.discoverapi.controllers.exceptions.StandardError;
import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Artist;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.services.ArtistService;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/artists")
@AllArgsConstructor
@Tag(name = "Artist Controller")
public class ArtistController {
    private ArtistService artistService;

    // get every stored artist
    @Operation(description = "Gets all artists in a paginated way.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    ref = "#/components/responses/artistPaginatedResponse"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<Map<String, Object>> findAll(
            @Parameter(description = "The number of the page that should be retrieved (starting with 1).")
            @RequestParam(defaultValue = "1") int pageNumber,
            @Parameter(description = "Number of items in each page.")
            @RequestParam(defaultValue = "3") int pageSize
    ){
        Map<String, Object> paginatedArtists = artistService.findAll(pageNumber, pageSize);
        return ResponseEntity.ok().body(paginatedArtists);
    }

    // get a specific artist
    @Operation(description = "Returns a specific artist.")
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
    public ResponseEntity<Artist> findById(
            @Parameter(description="Id of the artist to be retrieved.") @PathVariable long id){
        // retrieves the artist and returns it
        Artist foundArtist = artistService.findById(id);
        return ResponseEntity.ok(foundArtist);
    }

    // create a single artist
    @Operation(description = "Creates an artist.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @PostMapping(value = "", produces = "application/json")
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
    @Operation(description = "Updates a specific artist's name")
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
    public ResponseEntity<Artist> updateById(
            @Parameter(description="Id of the artist to be updated.") @PathVariable long id,
            @RequestBody Artist artistDataToUpdate){
        // updates the artist
        Artist updatedArtist = artistService.update(id, artistDataToUpdate);

        // returns the updated artist
        return ResponseEntity.ok(updatedArtist);
    }

    // delete a single artist by id
    @Operation(description = "Deletes a specific artist.")
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
    public ResponseEntity<Artist> deleteById(
            @Parameter(description="id of the artist to be deleted.") @PathVariable long id){
        // deletes the artist
        artistService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ------- '/albums' SUBRESOURCE -------
    // find all the artists' albums
    @Operation(description = "Returns all albums from an artist")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping(value = "{artistId}/albums", produces = "application/json")
    public ResponseEntity<Set<Album>> findAllAlbumsOfArtist(
            @Parameter(description="Id of the artist that the albums should be from.") @PathVariable long artistId){
        // find the artist's albums
        Set<Album> foundAlbums = artistService.findAllAlbumsOfArtist(artistId);
        return ResponseEntity.ok(foundAlbums);
    }

    // add an album to the artist's album
    @Operation(description = "Adds an album to an artist's list of albums.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @PutMapping(value = "{artistId}/albums/{albumId}", produces = "application/json")
    public ResponseEntity<Album> addAlbumToArtist(
            @Parameter(description="Id of the artist to which the album will be added.") @PathVariable long artistId,
            @Parameter(description="Id of the album to be added.") @PathVariable long albumId){
        // add the album to the list of the artist's albums
        Album addedAlbum = artistService.addAlbumToArtist(artistId, albumId);
        return ResponseEntity.ok(addedAlbum);
    }

    // delete an existing album from a given artist
    @Operation(description = "Deletes an album from an artist's list of albums.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = @Content),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @DeleteMapping(value = "{artistId}/albums/{albumId}", produces = "application/json")
    public ResponseEntity<Album> removeAlbumFromArtist(
            @Parameter(description="Id of the artist from which the album will be removed.") @PathVariable long artistId,
            @Parameter(description="Id of the album to be removed from the artist's list of albums.") @PathVariable long albumId){
        // remove album from the artist's list of albums
        artistService.deleteAlbumFromArtist(artistId, albumId);
        return ResponseEntity.noContent().build();
    }

    // ------- '/tracks' SUBRESOURCE -------
    // find all the artists' tracks
    @Operation(description = "Returns all tracks from a artist.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping(value = "{artistId}/tracks", produces = "application/json")
    public ResponseEntity<Set<Track>> findAllTracksOfArtist(
            @Parameter(description="Id of the artist that the tracks should be from.") @PathVariable long artistId){
        // find the artist's tracks
        Set<Track> foundTracks = artistService.findAllTracksOfArtist(artistId);
        return ResponseEntity.ok(foundTracks);
    }

    // add a track to the artist's tracks
    @Operation(description = "Adds a track to the artist's list of tracks.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @PutMapping(value = "{artistId}/tracks/{trackId}", produces = "application/json")
    public ResponseEntity<Track> addTrackToArtist(
            @Parameter(description="Id of the artist to which the album will be added.") @PathVariable long artistId,
            @Parameter(description="Id of the track to be added.") @PathVariable long trackId){
        // add the track to the list of the artist's tracks
        Track addedTrack = artistService.addTrackToArtist(artistId, trackId);
        return ResponseEntity.ok(addedTrack);
    }

    // delete an existing track from a given artist
    @Operation(description = "Deletes a track from the artist's list of tracks.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = @Content),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @DeleteMapping(value = "{artistId}/tracks/{trackId}", produces = "application/json")
    public ResponseEntity<Track> removeTrackFromArtist(
            @Parameter(description="Id of the artist from which the track should be removed") @PathVariable long artistId,
            @Parameter(description="Id of the track to be removed from the artist's list of tracks") @PathVariable long trackId){
        // remove track from the artist's list of tracks
        artistService.deleteTrackFromArtist(artistId, trackId);
        return ResponseEntity.noContent().build();
    }

    // ------- '/image' SUBRESOURCE --------

    // defines an artist's image
    @Operation(description = "Defines an artist's profile image by multipart/form-data upload.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @PutMapping(value = "{artistId}/image", produces = "application/json")
    public ResponseEntity<Artist> setImage(
            @Parameter(description="Id of the artist that the image will be uploaded to.") @PathVariable long artistId,
            @RequestParam MultipartFile image){
        artistService.setArtistImage(artistId, image);
        return ResponseEntity.ok().build();
    }

    // gets an artist's image
    @Operation(description = "Gets an artist's profile image.")
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
    @GetMapping(value = "{artistId}/image", produces = "image/png")
    public ResponseEntity<ByteArrayResource> getImage(
            @Parameter(description="Id of the artist that the retrieved image should be of.") @PathVariable long artistId){
        byte[] imageData = artistService.getArtistImage(artistId);
        ByteArrayResource resource = new ByteArrayResource(imageData);
        return ResponseEntity
                .ok()
                .header("Content-type", "image/png")
                .body(resource);
    }

    // --- '/search' SUBRESOURCES ---
    @Operation(description = "Searches for an artist by their name.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    ref ="#/components/responses/artistSearchResponse"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity<Map<String, Object>> findByNameContaining(
            @Parameter(description="The artist's name that should be searched for.") @RequestParam String name,
            @Parameter(description="The number of the page that should be retrieved (starting with 1).") @RequestParam(defaultValue = "1") int pageNumber,
            @Parameter(description="Number of items in each page.") int pageSize){
        Map<String, Object> response = artistService.findByNameContaining(name, pageNumber, pageSize);
        return ResponseEntity.ok().body(response);
    }
}
