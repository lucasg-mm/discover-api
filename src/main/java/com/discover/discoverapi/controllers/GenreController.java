package com.discover.discoverapi.controllers;

import com.discover.discoverapi.controllers.exceptions.StandardError;
import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Artist;
import com.discover.discoverapi.entities.Genre;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.services.GenreService;
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
import java.util.Set;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/genres")
@AllArgsConstructor
@Tag(name = "Genre Controller")
public class GenreController {
    private GenreService genreService;

    // get every stored genre
    @Operation(description = "Gets all stored genres (paginated).")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    ref = "#/components/responses/genrePaginatedResponse"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
    })
    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<Map<String, Object>> findAll(
            @Parameter(description = "The number of the page that should be retrieved (starting with 1).")
            @RequestParam(defaultValue = "1") int pageNumber,
            @Parameter(description = "Number of items in each page.")
            @RequestParam(defaultValue = "3") int pageSize
    ){
        Map<String, Object> paginatedGenres = genreService.findAll(pageNumber, pageSize);
        return ResponseEntity.ok(paginatedGenres);
    }

    // get a specific genre by id
    @Operation(description = "Gets a specific genre.")
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
    public ResponseEntity<Genre> findById(
            @Parameter(description="The id from the genre to be retrieved.") @PathVariable long id){
        // retrieves the genre and returns it
        Genre foundGenre = genreService.findById(id);
        return  ResponseEntity.ok(foundGenre);
    }

    // create a single genre
    @Operation(description = "Creates a specific Genre")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @PostMapping(value = "", produces = "application/json")
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
    @Operation(description = "Updates a specific Genre")
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
    public ResponseEntity<Genre> updateById(
            @Parameter(description="Id from the genre to be removed.") @PathVariable long id,
            @RequestBody Genre genreDataToUpdate){
        // updates the genre
        Genre updatedGenre = genreService.update(id, genreDataToUpdate);

        // returns the updated genre
        return ResponseEntity.ok(updatedGenre);
    }

    // delete a single genre by id
    @Operation(description = "Deletes a specific genre.")
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
    public ResponseEntity<Genre> deleteById(
            @Parameter(description="Id from the genre to be removed.") @PathVariable long id){
        // deletes the genre
        genreService.deleteById(id);

        // no content response
        return ResponseEntity.noContent().build();
    }

    // ------- '/albums' SUBRESOURCE -------
    // find all the genres' albums
    @Operation(description = "Gets all albums from a genre's list of albums.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping(value = "{genreId}/albums", produces = "application/json")
    public ResponseEntity<Set<Album>> findAllAlbumsOfGenre(
            @Parameter(description="Id of the genre that the albums should be from.") @PathVariable long genreId){
        // find the genre's albums
        Set<Album> foundAlbums = genreService.findAllAlbumsOfGenre(genreId);
        return ResponseEntity.ok(foundAlbums);
    }

    // add an album to the genre's album
    @Operation(description = "Adds an album to a genre's list of albums.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @PutMapping(value = "{genreId}/albums/{albumId}", produces = "application/json")
    public ResponseEntity<Album> addAlbumToGenre(
            @Parameter(description="Id of the genre that the album should be added to.") @PathVariable long genreId,
            @Parameter(description="Id of the album to be added to the genre's list of albums.") @PathVariable long albumId){
        // add the album to the list of the genre's albums
        Album addedAlbum = genreService.addAlbumToGenre(genreId, albumId);
        return ResponseEntity.ok(addedAlbum);
    }

    // delete an existing album from a given genre
    @Operation(description = "Deletes an album from a genre's list of albums.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = @Content),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @DeleteMapping(value = "{genreId}/albums/{albumId}", produces = "application/json")
    public ResponseEntity<Album> removeAlbumFromGenre(
            @Parameter(description="Id of the genre that the album should be removed from.") @PathVariable long genreId,
            @Parameter(description="Id of the album to be removed from the genre's list of albums.") @PathVariable long albumId){
        // remove album from the genre's list of albums
        genreService.deleteAlbumFromGenre(genreId, albumId);
        return ResponseEntity.noContent().build();
    }

    // ------- '/artists' SUBRESOURCE -------
    // find all the genres' artists
    @Operation(description = "Gets all artists from a genre's list of artists.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping(value = "{genreId}/artists", produces = "application/json")
    public ResponseEntity<Set<Artist>> findAllArtistsOfGenre(
            @Parameter(description="Id of the genre that the artists should be from.") @PathVariable long genreId){
        // find the genre's artists
        Set<Artist> foundArtists = genreService.findAllArtistsOfGenre(genreId);
        return ResponseEntity.ok(foundArtists);
    }

    // add an artist to the genre's artist
    @Operation(description = "Adds an artist to a genre's list of artists.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @PutMapping(value = "{genreId}/artists/{artistId}", produces = "application/json")
    public ResponseEntity<Artist> addArtistToGenre(
            @Parameter(description="Id of the genre that the artist should be added to.") @PathVariable long genreId,
            @Parameter(description="Id of the artist that should be added to the genre's list of artists.") @PathVariable long artistId){
        // add the artist to the list of the genre's artists
        Artist addedArtist = genreService.addArtistToGenre(genreId, artistId);
        return ResponseEntity.ok(addedArtist);
    }

    // delete an existing artist from a given genre
    @Operation(description = "Deletes an artist from a genre's list of artists.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = @Content),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @DeleteMapping(value = "{genreId}/artists/{artistId}", produces = "application/json")
    public ResponseEntity<Artist> removeArtistFromGenre(
            @Parameter(description="Id of the genre that the artist should be removed from.") @PathVariable long genreId,
            @Parameter(description="Id of the artist that should be removed from the genre's list of artists.") @PathVariable long artistId){
        // remove artist from the genre's list of artists
        genreService.deleteArtistFromGenre(genreId, artistId);
        return ResponseEntity.noContent().build();
    }

    // ------- '/tracks' SUBRESOURCE -------
    // find all the genres' tracks
    @Operation(description = "Gets all tracks from a genre's list of tracks.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping(value = "{genreId}/tracks", produces = "application/json")
    public ResponseEntity<Set<Track>> findAllTracksOfGenre(
            @Parameter(description="Id of the genre that the tracks should be from.") @PathVariable long genreId){
        // find the genre's tracks
        Set<Track> foundTracks = genreService.findAllTracksOfGenre(genreId);
        return ResponseEntity.ok(foundTracks);
    }

    // add a track to the genre's track
    @Operation(description = "Adds a track to a genre's list of tracks.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @PutMapping(value = "{genreId}/tracks/{trackId}", produces = "application/json")
    public ResponseEntity<Track> addTrackToGenre(
            @Parameter(description="Id of the genre that the track should be added to.")  @PathVariable long genreId,
            @Parameter(description="Id of the track to be added to the genre's list of tracks.") @PathVariable long trackId){
        // add the track to the list of the genre's tracks
        Track addedTrack = genreService.addTrackToGenre(genreId, trackId);
        return ResponseEntity.ok(addedTrack);
    }

    // delete an existing track from a given genre
    @Operation(description = "Deletes a track from a genre's list of tracks.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = @Content),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @DeleteMapping(value = "{genreId}/tracks/{trackId}", produces = "application/json")
    public ResponseEntity<Track> removeTrackFromGenre(
            @Parameter(description="Id of the genre that the track should be removed from.") @PathVariable long genreId,
            @Parameter(description="Id of the track to be removed from the genre's list of tracks.") @PathVariable long trackId){
        // remove track from the genre's list of tracks
        genreService.deleteTrackFromGenre(genreId, trackId);
        return ResponseEntity.noContent().build();
    }

    // --- '/search' SUBRESOURCES ---
    @Operation(description = "Searches for genres by their names.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    ref ="#/components/responses/genreSearchResponse"),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity<Map<String, Object>> findByNameContaining(
            @Parameter(description="The genre's name that should be searched for.") @RequestParam String name,
            @Parameter(description="The number of the page that should be retrieved (starting with 1).") @RequestParam(defaultValue = "1") int pageNumber,
            @Parameter(description="Number of items in each page.") @RequestParam(defaultValue = "3") int pageSize){
        Map<String, Object> response = genreService.findByNameContaining(name, pageNumber, pageSize);
        return ResponseEntity.ok().body(response);
    }
}
