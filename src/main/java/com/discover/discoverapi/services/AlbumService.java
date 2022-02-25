package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Artist;
import com.discover.discoverapi.entities.Genre;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.repositories.AlbumRepository;
import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final ArtistService artistService;
    private final GenreService genreService;
    private final TrackService trackService;

    // find all
    public List<Album> findAll(){
        return albumRepository.findAll();
    }

    // find by id
    public Album findById(long id){
        return albumRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Album of id " + id + " not found."));
    }

    // update by id
    public Album update(long id, Album toUpdate){
        // finds the album to be updated
        Album foundAlbum = findById(id);

        // updates every field
        foundAlbum.setLabel(toUpdate.getLabel());
        foundAlbum.setTitle(toUpdate.getTitle());
        foundAlbum.setCoverArtUrl(toUpdate.getCoverArtUrl());
        foundAlbum.setTracks(toUpdate.getTracks());
        foundAlbum.setLength(toUpdate.getLength());
        foundAlbum.setGenres(toUpdate.getGenres());
        foundAlbum.setReleaseDate(toUpdate.getReleaseDate());
        foundAlbum.setArtists(toUpdate.getArtists());

        // saves
        return albumRepository.save(foundAlbum);
    }

    // create
    public Album create(Album album){
        album.setId(0);  // shouldn't be null?
        return albumRepository.save(album);
    }

    // delete by id
    public void deleteById(long id){
        if (albumRepository.existsById(id)){
            albumRepository.deleteById(id);
        }
        else{
            throw new ObjectNotFoundException("Album of id " + id + " not found.");
        }
    }

    // find all the main artists that recorded the album
    public List<Artist> findAllArtistsOfAlbum(long albumId){
        Album foundAlbum = findById(albumId);
        return foundAlbum.getArtists();
    }

    // add artist to list of artists
    public Artist addArtistToAlbum(long albumId, long artistId){
        // finds the album by id
        Album foundAlbum = findById(albumId);

        // finds the artist by id
        Artist foundArtist = artistService.findById(artistId);

        // gets album's artists
        foundAlbum.getArtists().add(foundArtist);

        return foundArtist;
    }

    // delete an artist from the list of artists from an album
    public void deleteArtistFromAlbum(long albumId, long artistId){
        // finds the album by id
        Album foundAlbum = findById(albumId);

        // finds the artist by id
        Artist foundArtist = artistService.findById(artistId);

        // delete from the album's artists
        foundAlbum.getArtists().remove(foundArtist);
    }

    // find all the album's genres
    public List<Genre> findAllGenresOfAlbum(long albumId){
        Album foundAlbum = findById(albumId);
        return foundAlbum.getGenres();
    }

    // add genre to an album's list of genres
    public Genre addGenreToAlbum(long albumId, long genreId){
        // finds the album by id
        Album foundAlbum = findById(albumId);

        // finds the genre by id
        Genre foundGenre = genreService.findById(genreId);

        // gets album's genres
        foundAlbum.getGenres().add(foundGenre);

        return foundGenre;
    }

    // delete a genre from the list of genres from an album
    public void deleteGenreFromAlbum(long albumId, long genreId){
        // finds the album by id
        Album foundAlbum = findById(albumId);

        // finds the genre by id
        Genre foundGenre = genreService.findById(genreId);

        // delete from the album's genres
        foundAlbum.getGenres().remove(foundGenre);
    }

    // find all the album's tracks
    public List<Track> findAllTracksOfAlbum(long albumId){
        Album foundAlbum = findById(albumId);
        return foundAlbum.getTracks();
    }

    // add track to an album's list of tracks
    public Track addTrackToAlbum(long albumId, long trackId){
        // finds the album by id
        Album foundAlbum = findById(albumId);

        // finds the track by id
        Track foundTrack = trackService.findById(trackId);

        // gets album's tracks
        foundAlbum.getTracks().add(foundTrack);

        return foundTrack;
    }

    // delete a track from the list of tracks from an album
    public void deleteTrackFromAlbum(long albumId, long trackId){
        // finds the album by id
        Album foundAlbum = findById(albumId);

        // finds the track by id
        Track foundTrack = trackService.findById(trackId);

        // delete from the album's tracks
        foundAlbum.getTracks().remove(foundTrack);
    }
}
