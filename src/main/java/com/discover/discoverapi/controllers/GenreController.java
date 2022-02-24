package com.discover.discoverapi.controllers;

import com.discover.discoverapi.entities.Genre;
import com.discover.discoverapi.services.GenreService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/genres")
@AllArgsConstructor
public class GenreController {
    private GenreService genreService;

    // get every stored genre
    @GetMapping("")
    public ResponseEntity<List<Genre>> findAll(){
        List<Genre> allGenres = genreService.findAll();
        return ResponseEntity.ok(allGenres);
    }

    // get a specific genre by id
    @GetMapping("/{id}")
    public ResponseEntity<Genre> findById(@PathVariable long id){
        // retrieves the genre and returns it
        Genre foundGenre = genreService.findById(id);
        return  ResponseEntity.ok(foundGenre);
    }

    // create a single genre
    @PostMapping("")
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
    @PutMapping("/{id}")
    public ResponseEntity<Genre> updateById(@PathVariable long id, @RequestBody Genre genreDataToUpdate){
        // updates the genre
        Genre updatedGenre = genreService.update(id, genreDataToUpdate);

        // returns the updated genre
        return ResponseEntity.ok(updatedGenre);
    }

    // delete a single genre by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Genre> deleteById(@PathVariable long id){
        // deletes the genre
        genreService.deleteById(id);

        // no content response
        return ResponseEntity.noContent().build();
    }
}
