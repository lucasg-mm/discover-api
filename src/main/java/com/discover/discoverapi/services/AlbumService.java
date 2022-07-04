package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.repositories.AlbumRepository;
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
public class AlbumService {
    private AlbumRepository albumRepository;
    private TrackService trackService;
    private UploaderDownloader imageUploaderDownloader;

    // find all
    public Map<String, Object> findAll(
            @Min(value = 1, message = "'pageNumber' parameter should be greater or equal to 1.") int pageNumber,
            @Min(value = 3, message = "'pageSize' parameter should be greater or equal to 3.") int pageSize) {

        // declarations and instantiations
        Map<String, Object> response = new HashMap<>();  // the response that should be sent back to the user...
        Page<Album> pageWithAlbums;  // the page object with the albums
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);  // instantiates the Pageable object

        // retrieve albums in the given page
        pageWithAlbums = albumRepository.findAll(pageable);

        // mounts the response and returns it
        response.put("items", pageWithAlbums.getContent());
        response.put("totalItems", pageWithAlbums.getTotalElements());
        response.put("totalPages", pageWithAlbums.getTotalPages());

        return response;
    }

    // find by id
    public Album findById(long id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Album of id " + id + " not found."));
    }

    // update by id
    public Album update(long id, Album toUpdate) {
        // finds the album to be updated
        Album foundAlbum = findById(id);

        // updates every field
        foundAlbum.setLabel(toUpdate.getLabel());
        foundAlbum.setTitle(toUpdate.getTitle());
        foundAlbum.setLength(toUpdate.getLength());
        foundAlbum.setReleaseDate(toUpdate.getReleaseDate());

        // saves
        return albumRepository.save(foundAlbum);
    }

    // create
    public Album create(Album album) {
        album.setId(null);  // shouldn't be null?
        return albumRepository.save(album);
    }

    // delete by id
    public void deleteById(long id) {
        if (albumRepository.existsById(id)) {
            albumRepository.deleteById(id);
        } else {
            throw new ObjectNotFoundException("Album of id " + id + " not found.");
        }
    }

    // find all the album's tracks
    public Set<Track> findAllTracksOfAlbum(long albumId) {
        Album foundAlbum = findById(albumId);
        return foundAlbum.getTracks();
    }

    // add track to an album's list of tracks
    public Track addTrackToAlbum(long albumId, long trackId) {
        albumRepository.addTrack(albumId, trackId);
        return trackService.findById(trackId);
    }

    // delete a track from the list of tracks from an album
    public void deleteTrackFromAlbum(long albumId, long trackId) {
        albumRepository.removeTrack(albumId, trackId);
    }

    // uploads an image as the cover of an album
    public void setAlbumCover(long albumId, MultipartFile file) {
        // retrieves the album
        Album foundAlbum = findById(albumId);

        // gets the data related to the cover art's location
        String foundAlbumCoverArtPath = foundAlbum.getCoverArtPath();
        String foundAlbumCoverArtFileName = foundAlbum.getCoverArtFileName();

        if (foundAlbumCoverArtPath != null && foundAlbumCoverArtFileName != null){
            // if the data is not null, use it to upload the file (it's idempotent)
            imageUploaderDownloader.upload(file, foundAlbumCoverArtPath, foundAlbumCoverArtFileName);
        }
        else{
            // if the data is null, create a file name and a path
            String newFileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            String newFilePath = "album-covers";

            // use it to upload the image
            imageUploaderDownloader.upload(file, newFilePath, newFileName);

            // saves the new data in the database
            foundAlbum.setCoverArtFileName(newFileName);
            foundAlbum.setCoverArtPath(newFilePath);
            albumRepository.save(foundAlbum);
        }
    }

    // downloads the cover art image
    public byte[] getAlbumCover(long albumId){
        // gets album and its cover location data
        Album foundAlbum = findById(albumId);
        String foundAlbumCoverArtPath = foundAlbum.getCoverArtPath();
        String foundAlbumCoverArtFileName = foundAlbum.getCoverArtFileName();

        // download the cover if the location data is not null
        if (foundAlbumCoverArtPath != null && foundAlbumCoverArtFileName != null){
            return imageUploaderDownloader.download(foundAlbumCoverArtPath, foundAlbumCoverArtFileName);
        }
        else{
            throw new FailedToDownloadException("Album does not have a cover art.");
        }
    }

    // find albums with a title that contains the 'title' param, and returns it in a
    // paginated way
    public Map<String, Object> findByTitleContaining(
            @NotEmpty(message = "'title' parameter shouldn't be empty.") String title,
            @Min(value = 1, message = "'pageNumber' parameter should be greater or equal to 1.") int pageNumber,
            @Min(value = 3, message = "'pageSize' parameter should be greater or equal to 3.") int pageSize){

        // declarations and instantiations
        Map<String, Object> response = new HashMap<>();  // the response that should be sent back to the client
        Page<Album> pageWithAlbums;  // the page object with the albums
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);  // instantiates the Pageable object

        // retrieves albums in the given page
        pageWithAlbums = albumRepository.findByTitleContainingIgnoreCase(title, pageable);

        // mounts the response and returns it
        response.put("items", pageWithAlbums.getContent());
        response.put("totalItems", pageWithAlbums.getTotalElements());
        response.put("totalPages", pageWithAlbums.getTotalPages());

        return response;
    }
}
