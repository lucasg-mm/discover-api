package com.discover.discoverapi.controllers;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.services.AlbumService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/albums")
@AllArgsConstructor
public class AlbumController {
    private AlbumService albumService;

    // get every stored album
    @GetMapping("")
    public ResponseEntity<List<Album>> findAll(){
        List<Album> allAlbums = albumService.findAll();
        return ResponseEntity.ok().body(allAlbums);
    }

    // get a specific album
    @GetMapping("/{id}")
    public ResponseEntity<Album> findById(@PathVariable long id){
        // retrieves the album
        Album foundAlbum = albumService.findById(id);
        return ResponseEntity.ok().body(foundAlbum);
    }

    // create a single album
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
    @PutMapping("/{id}")
    public ResponseEntity<Album> updateById(@PathVariable long id, @RequestBody Album albumDataToUpdate){
        // updates the album
        Album updatedAlbum = albumService.update(id,albumDataToUpdate);

        return ResponseEntity.ok().body(updatedAlbum);
    }

    // delete a single album
    @DeleteMapping("/{id}")
    public ResponseEntity<Album> deleteById(@PathVariable long id){
        // deletes the album
        albumService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
