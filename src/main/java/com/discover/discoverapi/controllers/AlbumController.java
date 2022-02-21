package com.discover.discoverapi.controllers;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.services.AlbumService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/albums")
@AllArgsConstructor
public class AlbumController {
    private AlbumService albumService;

    @GetMapping("/")
    public ResponseEntity<List<Album>> findAll(){
        List<Album> allAlbums = albumService.findAll();
        return ResponseEntity.ok().body(allAlbums);
    }
}
