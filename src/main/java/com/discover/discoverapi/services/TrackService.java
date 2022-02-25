package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Artist;
import com.discover.discoverapi.entities.Genre;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.repositories.TrackRepository;
import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackService {
    private TrackRepository trackRepository;
    private ArtistService artistService;
    private AlbumService albumService;
    private GenreService genreService;

    // SETTERS
    @Autowired
    @Lazy
    public void setTrackRepository(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Autowired
    @Lazy
    public void setArtistService(ArtistService artistService) {
        this.artistService = artistService;
    }

    @Autowired
    @Lazy
    public void setAlbumService(AlbumService albumService) {
        this.albumService = albumService;
    }

    @Autowired
    @Lazy
    public void setGenreService(GenreService genreService) {
        this.genreService = genreService;
    }

    // find a single track by its id
    public Track findById(long id){
        return trackRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Track of id " + id + " not found."));
    }

    // find every single stored track
    public List<Track> findAll(){
        return trackRepository.findAll();
    }

    // create a single track
    public Track create(Track toCreate){
        toCreate.setId(0);
        return trackRepository.save(toCreate);
    }

    // update a single track by id
    public Track update(long id, Track toUpdate){
        // retrieves the track
        Track retrievedTrack = findById(id);

        // updates its fields
        retrievedTrack.setArtists(toUpdate.getArtists());
        retrievedTrack.setGenres(toUpdate.getGenres());
        retrievedTrack.setTitle(toUpdate.getTitle());
        retrievedTrack.setLyrics(toUpdate.getLyrics());
        retrievedTrack.setAlbum(toUpdate.getAlbum());
        retrievedTrack.setLength(toUpdate.getLength());

        // saves it
        return trackRepository.save(retrievedTrack);
    }

    // delete a single track by its id
    public void deleteById(long id){
        if (trackRepository.existsById(id)){
            trackRepository.deleteById(id);
        }
        else{
            throw new ObjectNotFoundException("Track of id " + id + " not found.");
        }
    }

    // find all the main artists that recorded a track
    public List<Artist> findAllArtistsOfTrack(long trackId){
        Track foundTrack = findById(trackId);
        return foundTrack.getArtists();
    }

    // add artist to the track's list of artists
    public Artist addArtistToTrack(long trackId, long artistId){
        // finds the track by id
        Track foundTrack = findById(trackId);

        // finds the artist by id
        Artist foundArtist = artistService.findById(artistId);

        // gets track's artists
        foundTrack.getArtists().add(foundArtist);
        trackRepository.save(foundTrack);

        return foundArtist;
    }

    // delete an artist from the track's list of artists
    public void deleteArtistFromTrack(long trackId, long artistId){
        // finds the track by id
        Track foundTrack = findById(trackId);

        // finds the artist by id
        Artist foundArtist = artistService.findById(artistId);

        // delete from the track's artists
        foundTrack.getArtists().remove(foundArtist);
        trackRepository.save(foundTrack);
    }

    // find all the genres from a track
    public List<Genre> findAllGenresOfTrack(long trackId){
        Track foundTrack = findById(trackId);
        return foundTrack.getGenres();
    }

    // add genre to the track's list of genres
    public Genre addGenreToTrack(long trackId, long genreId){
        // finds the track by id
        Track foundTrack = findById(trackId);

        // finds the genre by id
        Genre foundGenre = genreService.findById(genreId);

        // add the genre to the list
        foundTrack.getGenres().add(foundGenre);
        trackRepository.save(foundTrack);

        return foundGenre;
    }

    // delete a genre from the list of genres from an track
    public void deleteGenreFromTrack(long trackId, long genreId){
        // finds the track by id
        Track foundTrack = findById(trackId);

        // finds the genre by id
        Genre foundGenre = genreService.findById(genreId);

        // delete from the track's genres
        foundTrack.getGenres().remove(foundGenre);

        trackRepository.save(foundTrack);
    }

    // find the track's album
    public Album findAlbumOfTheTrack(long trackId){
        Track theTrack = findById(trackId);
        return theTrack.getAlbum();
    }

    // define an album as the track's album
    public Album assignAlbumToTrack(long trackId, long albumId){
        // finds the track and the album
        Track theTrack = findById(trackId);
        Album theAlbum = albumService.findById(albumId);

        // defines the album as the track's album
        theTrack.setAlbum(theAlbum);

        // saves it
        trackRepository.save(theTrack);

        // returns the album
        return theAlbum;
    }

    // delete the track's album
    public void unassignAlbumFromTrack(long trackId){
        // finds the track
        Track theTrack = findById(trackId);

        // defines the track's album as null
        theTrack.setAlbum(null);

        // updates the track with the new album value
        trackRepository.save(theTrack);
    }
}
