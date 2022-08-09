package com.discover.discoverapi.controllers;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/users")
@AllArgsConstructor
@Tag(name = "User Controller")
public class UserController {
    private UserService userService;

    // gets a user's list of liked albums
    @Operation(description = "Gets the list of albums liked by the user")
    @GetMapping(value = "/{userId}/albums", produces = "application/json")
    public ResponseEntity<Set<Album>> getLikedAlbums(@PathVariable long userId){
        Set<Album> likedAlbums = userService.getLikedAlbums(userId);
        return ResponseEntity.ok(likedAlbums);
    }

    // adds an album to the user's list of albums
    @PutMapping(value = "/{userId}/albums/{albumId}", produces = "application/json")
    public ResponseEntity<Album> addAlbumToLiked(@PathVariable long userId, @PathVariable long albumId){
        Album addedAlbum = userService.addAlbumToLiked(userId, albumId);

        return ResponseEntity.ok(addedAlbum);
    }

    // removes an album from the user's list of albums
    @DeleteMapping(value = "/{userId}/albums/{albumId}", produces = "application/json")
    public ResponseEntity<Album> removeAlbumFromLiked(@PathVariable long userId, @PathVariable long albumId){
        userService.removeAlbumFromLiked(userId, albumId);

        return ResponseEntity.noContent().build();
    }

//    // gets a user's list of liked tracks
//    public ResponseEntity<Set<Track>> getLikedTracks(){
//
//    }
//
//    // gets a user's list of liked artists
//    public ResponseEntity<Set<Artist>> getLikedArtist(){
//
//    }
//
//    // gets a user's list of liked genres
//    public ResponseEntity<Set<Genre>> getLikedGenres(){
//
//    }
}
