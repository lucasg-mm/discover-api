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
    @GetMapping(value = "/{username}/albums", produces = "application/json")
    public ResponseEntity<Set<Album>> getLikedAlbums(@PathVariable String username){
        Set<Album> likedAlbums = userService.getLikedAlbums(username);
        return ResponseEntity.ok(likedAlbums);
    }

    // adds an album to the user's list of albums
    @PutMapping(value = "/{username}/albums/{albumId}", produces = "application/json")
    public ResponseEntity<Album> addAlbumToLiked(@PathVariable String username, @PathVariable long albumId){
        Album addedAlbum = userService.addAlbumToLiked(username, albumId);
        return ResponseEntity.ok(addedAlbum);
    }

    // removes an album from the user's list of albums
    @DeleteMapping(value = "/{username}/albums/{albumId}", produces = "application/json")
    public ResponseEntity<Album> removeAlbumFromLiked(@PathVariable String username, @PathVariable long albumId){
        userService.removeAlbumFromLiked(username, albumId);
        return ResponseEntity.noContent().build();
    }
}
