package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Artist;
import com.discover.discoverapi.entities.Genre;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.repositories.ArtistRepository;
import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final AlbumService albumService;
    private final TrackService trackService;
    private final GenreService genreService;

    // find by id
    public Artist findById(long id){
        return artistRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Artist of id " + id + " not found."));
    }

    // find all artists
    public List<Artist> findAll(){
        return artistRepository.findAll();
    }

    // create an artist
    public Artist create(Artist toCreate){
        toCreate.setId(0);
        return artistRepository.save(toCreate);
    }

    // update an artist
    public Artist update(long id, Artist toUpdate){
        // retrieves the artist with the given id
        Artist retrievedArtist = findById(id);

        // updates retrieved artist properties
        retrievedArtist.setImageURL(toUpdate.getImageURL());
        retrievedArtist.setName(toUpdate.getName());
        retrievedArtist.setGenres(toUpdate.getGenres());
        retrievedArtist.setAlbums(toUpdate.getAlbums());
        retrievedArtist.setTracks(toUpdate.getTracks());

        // saves artist
        return artistRepository.save(retrievedArtist);
    }

    // delete an artist by id
    public void deleteById(long id){
        if (artistRepository.existsById(id)){
            artistRepository.deleteById(id);
        }
        else{
            throw new ObjectNotFoundException("Artist of id " + id + " not found.");
        }
    }

    // find all the albums from an artist
    public List<Album> findAllAlbumsOfArtist(long artistId){
        Artist foundArtist = findById(artistId);
        return foundArtist.getAlbums();
    }

    // add album to the artist's list of albums
    public Album addAlbumToArtist(long artistId, long albumId){
        // finds the artist by id
        Artist foundArtist = findById(artistId);

        // finds the album by id
        Album foundAlbum = albumService.findById(albumId);

        // add the album to the list
        foundArtist.getAlbums().add(foundAlbum);

        return foundAlbum;
    }

    // delete an album from the list of albums from an artist
    public void deleteAlbumFromArtist(long artistId, long albumId){
        // finds the artist by id
        Artist foundArtist = findById(artistId);

        // finds the album by id
        Album foundAlbum = albumService.findById(albumId);

        // delete from the artist's albums
        foundArtist.getAlbums().remove(foundAlbum);
    }

    // find all the genres from an artist
    public List<Genre> findAllGenresOfArtist(long artistId){
        Artist foundArtist = findById(artistId);
        return foundArtist.getGenres();
    }

    // add genre to the artist's list of genres
    public Genre addGenreToArtist(long artistId, long genreId){
        // finds the artist by id
        Artist foundArtist = findById(artistId);

        // finds the genre by id
        Genre foundGenre = genreService.findById(genreId);

        // add the genre to the list
        foundArtist.getGenres().add(foundGenre);

        return foundGenre;
    }

    // delete a genre from the list of genres from an artist
    public void deleteGenreFromArtist(long artistId, long genreId){
        // finds the artist by id
        Artist foundArtist = findById(artistId);

        // finds the genre by id
        Genre foundGenre = genreService.findById(genreId);

        // delete from the artist's genres
        foundArtist.getGenres().remove(foundGenre);
    }

    // find all the tracks from an artist
    public List<Track> findAllTracksOfArtist(long artistId){
        Artist foundArtist = findById(artistId);
        return foundArtist.getTracks();
    }

    // add track to the artist's list of tracks
    public Track addTrackToArtist(long artistId, long trackId){
        // finds the artist by id
        Artist foundArtist = findById(artistId);

        // finds the track by id
        Track foundTrack = trackService.findById(trackId);

        // add the track to the list
        foundArtist.getTracks().add(foundTrack);

        return foundTrack;
    }

    // delete a track from the list of tracks from an artist
    public void deleteTrackFromArtist(long artistId, long trackId){
        // finds the artist by id
        Artist foundArtist = findById(artistId);

        // finds the track by id
        Track foundTrack = trackService.findById(trackId);

        // delete from the artist's tracks
        foundArtist.getTracks().remove(foundTrack);
    }
}
