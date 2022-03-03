package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Artist;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.repositories.ArtistRepository;
import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ArtistService {
    private ArtistRepository artistRepository;
    private AlbumService albumService;
    private TrackService trackService;

    // find by id
    @Transactional
    public Artist findById(long id){
        return artistRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Artist of id " + id + " not found."));
    }

    // find all artists
    @Transactional
    public List<Artist> findAll(){
        return artistRepository.findAll();
    }

    // create an artist
    @Transactional
    public Artist create(Artist toCreate){
        toCreate.setId(0);
        return artistRepository.save(toCreate);
    }

    // update an artist
    @Transactional
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
    @Transactional
    public void deleteById(long id){
        if (artistRepository.existsById(id)){
            artistRepository.deleteById(id);
        }
        else{
            throw new ObjectNotFoundException("Artist of id " + id + " not found.");
        }
    }

    // find all the albums from an artist
    @Transactional
    public Set<Album> findAllAlbumsOfArtist(long artistId){
        Artist foundArtist = findById(artistId);
        return foundArtist.getAlbums();
    }

    // add album to the artist's list of albums
    @Transactional
    public Album addAlbumToArtist(long artistId, long albumId){
        // finds the artist by id
        Artist foundArtist = findById(artistId);

        // finds the album by id
        Album foundAlbum = albumService.findById(albumId);

        // add the album to the list
        foundArtist.getAlbums().add(foundAlbum);
        artistRepository.save(foundArtist);

        return foundAlbum;
    }

    // delete an album from the list of albums from an artist
    @Transactional
    public void deleteAlbumFromArtist(long artistId, long albumId){
        // finds the artist by id
        Artist foundArtist = findById(artistId);

        // finds the album by id
        Album foundAlbum = albumService.findById(albumId);

        // delete from the artist's albums
        foundArtist.getAlbums().remove(foundAlbum);
        artistRepository.save(foundArtist);
    }

    // find all the tracks from an artist
    @Transactional
    public Set<Track> findAllTracksOfArtist(long artistId){
        Artist foundArtist = findById(artistId);
        return foundArtist.getTracks();
    }

    // add track to the artist's list of tracks
    @Transactional
    public Track addTrackToArtist(long artistId, long trackId){
        // finds the artist by id
        Artist foundArtist = findById(artistId);

        // finds the track by id
        Track foundTrack = trackService.findById(trackId);

        // add the track to the list
        foundArtist.getTracks().add(foundTrack);
        artistRepository.save(foundArtist);

        return foundTrack;
    }

    // delete a track from the list of tracks from an artist
    @Transactional
    public void deleteTrackFromArtist(long artistId, long trackId){
        // finds the artist by id
        Artist foundArtist = findById(artistId);

        // finds the track by id
        Track foundTrack = trackService.findById(trackId);

        // delete from the artist's tracks
        foundArtist.getTracks().remove(foundTrack);
        artistRepository.save(foundArtist);
    }
}
