package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Artist;
import com.discover.discoverapi.entities.Genre;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.repositories.GenreRepository;
import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Validated
@Service
@AllArgsConstructor
public class GenreService {
    private GenreRepository genreRepository;
    private AlbumService albumService;
    private TrackService trackService;
    private ArtistService artistService;

    // find a genre by id
    public Genre findById(long id){
        return genreRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Genre of id " + id + " not found."));
    }

    // find every single stored genre (paginated)
    public Map<String, Object> findAll(
            @Min(value = 1, message = "'pageNumber' parameter should be greater or equal to 1.") int pageNumber,
            @Min(value = 3, message = "'pageSize' parameter should be greater or equal to 3.") int pageSize){

        // declarations and instantiations
        Map<String, Object> response = new HashMap<>();  // the response that should be sent back to the user...
        Page<Genre> pageWithGenres;  // the page object with the genres
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);  // instantiates the Pageable object

        pageWithGenres = genreRepository.findAll(pageable);

        // mounts the response and returns it
        response.put("items", pageWithGenres.getContent());
        response.put("totalItems", pageWithGenres.getTotalElements());
        response.put("totalPages", pageWithGenres.getTotalPages());

        return response;
    }

    // create a single genre
    public Genre create(Genre toCreate){
        toCreate.setId(null);
        return genreRepository.save(toCreate);
    }

    // update a single genre (just its name)
    public Genre update(long id, Genre toUpdate){
        // retrieves the genre
        Genre retrievedGenre = findById(id);

        // updates retrieved genre's properties
        retrievedGenre.setName(toUpdate.getName());

        // saves
        return genreRepository.save(retrievedGenre);
    }

    // deletes a single genre
    public void deleteById(long id){
        if (genreRepository.existsById(id)){
            genreRepository.deleteById(id);

        }
        else{
            throw new ObjectNotFoundException("Genre of id " + id + " not found.");
        }
    }

    // find all the albums from a genre
    public Set<Album> findAllAlbumsOfGenre(long genreId){
        Genre foundGenre = findById(genreId);
        return foundGenre.getAlbums();
    }

    // add album to the genre's list of albums
    public Album addAlbumToGenre(long genreId, long albumId){
        genreRepository.addAlbum(genreId, albumId);
        return albumService.findById(albumId);
    }

    // delete an album from the list of albums from a genre
    public void deleteAlbumFromGenre(long genreId, long albumId){
        genreRepository.deleteAlbum(genreId, albumId);
    }

    // find all the tracks from a genre
    public Set<Track> findAllTracksOfGenre(long genreId){
        Genre foundGenre = findById(genreId);
        return foundGenre.getTracks();
    }

    // add track to the genre's list of tracks
    public Track addTrackToGenre(long genreId, long trackId){
        genreRepository.addTrack(genreId, trackId);
        return trackService.findById(trackId);
    }

    // delete a track from the list of tracks from a genre
    public void deleteTrackFromGenre(long genreId, long trackId){
        genreRepository.deleteTrack(genreId, trackId);
    }

    // find all the main artists that from a genre
    public Set<Artist> findAllArtistsOfGenre(long genreId){
        Genre foundGenre = findById(genreId);
        return foundGenre.getArtists();
    }

    // add artist to the genre's list of artists
    public Artist addArtistToGenre(long genreId, long artistId){
        genreRepository.addArtist(genreId, artistId);
        return artistService.findById(artistId);
    }

    // delete an artist from the genre's list of artists
    public void deleteArtistFromGenre(long genreId, long artistId){
        genreRepository.deleteArtist(genreId, artistId);
    }

    // find genres with a title that contains the 'name' param, and returns it in a
    // paginated way
    public Map<String, Object> findByNameContaining(
            @NotEmpty(message = "'name' parameter shouldn't be empty.") String name,
            @Min(value = 1, message = "'pageNumber' parameter should be greater or equal to 1.") int pageNumber,
            @Min(value = 3, message = "'pageSize' parameter should be greater or equal to 3.") int pageSize){

        // declarations and instantiations
        Map<String, Object> response = new HashMap<>();  // the response that should be sent back to the client
        Page<Genre> pageWithGenres;  // the page object with the artists
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);  // instantiates the Pageable object

        // retrieves genres in the given page
        pageWithGenres = genreRepository.findByNameContainingIgnoreCase(name, pageable);

        // mounts the response and return it
        response.put("items", pageWithGenres.getContent());
        response.put("totalItems", pageWithGenres.getTotalElements());
        response.put("totalPages", pageWithGenres.getTotalPages());

        return response;
    }
}
