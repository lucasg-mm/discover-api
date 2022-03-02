package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Artist;
import com.discover.discoverapi.entities.Genre;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.repositories.GenreRepository;
import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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

    // find every single genre
    public List<Genre> findAll(){
        return genreRepository.findAll();
    }

    // create a single genre
    public Genre create(Genre toCreate){
        toCreate.setId(0);
        return genreRepository.save(toCreate);
    }

    // update a single genre
    public Genre update(long id, Genre toUpdate){
        // retrieves the genre
        Genre retrievedGenre = findById(id);

        // updates retrieved genre's properties
        retrievedGenre.setArtists(toUpdate.getArtists());
        retrievedGenre.setName(toUpdate.getName());
        retrievedGenre.setTracks(toUpdate.getTracks());
        retrievedGenre.setAlbums(toUpdate.getAlbums());

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
        // finds the genre by id
        Genre foundGenre = findById(genreId);

        // finds the album by id
        Album foundAlbum = albumService.findById(albumId);

        // add the album to the list
        foundGenre.getAlbums().add(foundAlbum);
        genreRepository.save(foundGenre);

        return foundAlbum;
    }

    // delete an album from the list of albums from a genre
    public void deleteAlbumFromGenre(long genreId, long albumId){
        // finds the genre by id
        Genre foundGenre = findById(genreId);

        // finds the album by id
        Album foundAlbum = albumService.findById(albumId);

        // delete from the genre's albums
        foundGenre.getAlbums().remove(foundAlbum);
        genreRepository.save(foundGenre);
    }

    // find all the tracks from a genre
    public Set<Track> findAllTracksOfGenre(long genreId){
        Genre foundGenre = findById(genreId);
        return foundGenre.getTracks();
    }

    // add track to the genre's list of tracks
    public Track addTrackToGenre(long genreId, long trackId){
        // finds the genre by id
        Genre foundGenre = findById(genreId);

        // finds the track by id
        Track foundTrack = trackService.findById(trackId);

        // add the track to the list
        foundGenre.getTracks().add(foundTrack);
        genreRepository.save(foundGenre);

        return foundTrack;
    }

    // delete a track from the list of tracks from a genre
    public void deleteTrackFromGenre(long genreId, long trackId){
        // finds the genre by id
        Genre foundGenre = findById(genreId);

        // finds the track by id
        Track foundTrack = trackService.findById(trackId);

        // delete from the genre's tracks
        foundGenre.getTracks().remove(foundTrack);
        genreRepository.save(foundGenre);
    }

    // find all the main artists that from a genre
    public Set<Artist> findAllArtistsOfGenre(long genreId){
        Genre foundGenre = findById(genreId);
        return foundGenre.getArtists();
    }

    // add artist to the genre's list of artists
    public Artist addArtistToGenre(long genreId, long artistId){
        // finds the genre by id
        Genre foundGenre = findById(genreId);

        // finds the artist by id
        Artist foundArtist = artistService.findById(artistId);

        // gets genre's artists
        foundGenre.getArtists().add(foundArtist);
        genreRepository.save(foundGenre);

        return foundArtist;
    }

    // delete an artist from the genre's list of artists
    public void deleteArtistFromGenre(long genreId, long artistId){
        // finds the genre by id
        Genre foundGenre = findById(genreId);

        // finds the artist by id
        Artist foundArtist = artistService.findById(artistId);

        // delete from the genre's artists
        foundGenre.getArtists().remove(foundArtist);
        genreRepository.save(foundGenre);
    }
}
