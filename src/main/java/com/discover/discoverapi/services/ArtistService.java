package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Artist;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.repositories.ArtistRepository;
import com.discover.discoverapi.services.exceptions.FailedToDownloadException;
import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import com.discover.discoverapi.services.fileuploaddownload.UploaderDownloader;
import liquibase.util.file.FilenameUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.*;

@Validated
@Service
@AllArgsConstructor
public class ArtistService {
    private ArtistRepository artistRepository;
    private AlbumService albumService;
    private TrackService trackService;
    private UploaderDownloader imageUploaderDownloader;

    // find by id
    public Artist findById(long id){
        return artistRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Artist of id " + id + " not found."));
    }

    // find all artists
    public Map<String, Object> findAll(
            @Min(value = 1, message = "'pageNumber' parameter should be greater or equal to 1.") int pageNumber,
            @Min(value = 3, message = "'pageSize' parameter should be greater or equal to 3.") int pageSize){

        // declarations and instantiations
        Map<String, Object> response = new HashMap<>();  // the response that should be sent back to the user...
        Page<Artist> pageWithArtists;  // the page object with the artists
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);  // instantiates the Pageable object

        // retrieve artists in the given page
        pageWithArtists = artistRepository.findAll(pageable);

        // mounts the response and returns it
        response.put("items", pageWithArtists.getContent());
        response.put("totalItems", pageWithArtists.getTotalElements());
        response.put("totalPages", pageWithArtists.getTotalPages());

        return response;
    }

    // create an artist
    public Artist create(Artist toCreate){
        toCreate.setId(null);
        return artistRepository.save(toCreate);
    }

    // updates an artist (just its name)
    public Artist update(long id, Artist toUpdate){
        // retrieves the artist with the given id
        Artist retrievedArtist = findById(id);

        // updates retrieved artist properties (just the name)
        retrievedArtist.setName(toUpdate.getName());

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
    public Set<Album> findAllAlbumsOfArtist(long artistId){
        Artist foundArtist = findById(artistId);
        return foundArtist.getAlbums();
    }

    // add album to the artist's list of albums
    public Album addAlbumToArtist(long artistId, long albumId){
        artistRepository.addAlbum(artistId, albumId);
        return albumService.findById(albumId);
    }

    // delete an album from the list of albums from an artist
    public void deleteAlbumFromArtist(long artistId, long albumId){
        artistRepository.deleteAlbum(artistId, albumId);
    }

    // find all the tracks from an artist
    public Set<Track> findAllTracksOfArtist(long artistId){
        Artist foundArtist = findById(artistId);
        return foundArtist.getTracks();
    }

    // add track to the artist's list of tracks
    public Track addTrackToArtist(long artistId, long trackId){
        artistRepository.addTrack(artistId, trackId);
        return trackService.findById(trackId);
    }

    // delete a track from the list of tracks from an artist
    public void deleteTrackFromArtist(long artistId, long trackId){
        artistRepository.deleteTrack(artistId, trackId);
    }

    // uploads the artist's image
    public void setArtistImage(long artistId, MultipartFile file){
        // retrieves the artist
        Artist foundArtist = findById(artistId);

        String imagePath = foundArtist.getImagePath();
        String imageName = foundArtist.getImageFileName();

        // checks if artist has a path to an image saved in the database
        if (imagePath != null && imageName != null ){
            // if it has, use the same path to upload the new image
            imageUploaderDownloader.upload(file, imagePath, imageName);
        }
        else{
            // if the data is null, create the image name and path
            String newImageName = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            String newImagePath = "artists-images";

            // use it to upload the image
            imageUploaderDownloader.upload(file, newImagePath, newImageName);

            // saves the new data in the database
            foundArtist.setImagePath(newImagePath);
            foundArtist.setImageFileName(newImageName);
            artistRepository.save(foundArtist);
        }
    }

    // downloads the artist's image
    public byte[] getArtistImage(long artistId){
        // gets artist and its image location data
        Artist foundArtist = findById(artistId);
        String foundArtistImagePath = foundArtist.getImagePath();
        String foundArtistImageFileName = foundArtist.getImageFileName();

        // download the image if the location data is not null
        if (foundArtistImagePath != null && foundArtistImageFileName != null){
            return imageUploaderDownloader.download(foundArtistImagePath, foundArtistImageFileName);
        }
        else{
            throw new FailedToDownloadException("Artist does not have an image.");
        }
    }

    // find artists with a title that contains the 'name' param, and returns it in a
    // paginated way
    public Map<String, Object> findByNameContaining(
            @NotEmpty(message = "'name' parameter shouldn't be empty.") String name,
            @Min(value = 1, message = "'pageNumber' parameter should be greater or equal to 1.") int pageNumber,
            @Min(value = 3, message = "'pageSize' parameter should be greater or equal to 3.") int pageSize){

        // declarations and instantiations
        Map<String, Object> response = new HashMap<>();  // the response that should be sent back to the client
        Page<Artist> pageWithArtists;  // the page object with the albums
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);  // instantiates the Pageable object

        // retrieves artists in the given page
        pageWithArtists = artistRepository.findByNameContainingIgnoreCase(name, pageable);

        // mounts the response and return it
        response.put("items", pageWithArtists.getContent());
        response.put("totalItems", pageWithArtists.getTotalElements());
        response.put("totalPages", pageWithArtists.getTotalPages());

        return response;
    }
}
