package com.discover.discoverapi.controllers;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/users")
@AllArgsConstructor
@Tag(name = "User Controller")
public class UserController {
    private UserService userService;

    // gets a user's list of liked albums
    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<Set<Album>> getLikedAlbums(long userId){
        Set<Album> likedAlbums = userService.getLikedAlbums(userId);
        return ResponseEntity.ok(likedAlbums);
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
