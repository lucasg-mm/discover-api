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
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.*;

@Service
@AllArgsConstructor
public class AlbumService {
    private AlbumRepository albumRepository;
    private TrackService trackService;
    private UploaderDownloader imageUploaderDownloader;

    // find all
    @Transactional
    public List<Album> findAll() {
        return albumRepository.findAll();
    }

    // find by id
    @Transactional
    public Album findById(long id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Album of id " + id + " not found."));
    }

    // update by id
    @Transactional
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
    @Transactional
    public Album create(Album album) {
        album.setId(0);  // shouldn't be null?
        return albumRepository.save(album);
    }

    // delete by id
    @Transactional
    public void deleteById(long id) {
        if (albumRepository.existsById(id)) {
            albumRepository.deleteById(id);
        } else {
            throw new ObjectNotFoundException("Album of id " + id + " not found.");
        }
    }

    // find all the album's tracks
    @Transactional
    public Set<Track> findAllTracksOfAlbum(long albumId) {
        Album foundAlbum = findById(albumId);
        return foundAlbum.getTracks();
    }

    // add track to an album's list of tracks
    @Transactional
    public Track addTrackToAlbum(long albumId, long trackId) {
        // finds the album by id
        Album foundAlbum = findById(albumId);

        // finds the track by id
        Track foundTrack = trackService.findById(trackId);

        // gets album's tracks
        foundTrack.setAlbum(foundAlbum);
        albumRepository.save(foundAlbum);

        return foundTrack;
    }

    // delete a track from the list of tracks from an album
    @Transactional
    public void deleteTrackFromAlbum(long albumId, long trackId) {
        // finds the album by id
        Album foundAlbum = findById(albumId);

        // finds the track by id
        Track foundTrack = trackService.findById(trackId);

        // delete from the album's tracks
        foundAlbum.getTracks().remove(foundTrack);
        albumRepository.save(foundAlbum);
    }

    // uploads an image as the cover of an album
    @Transactional
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
    @Transactional
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
    @Transactional
    public Map<String, Object> findByTitleContaining(String title, int pageNumber, int pageSize){
        // declarations and instantiations
        Map<String, Object> response = new HashMap<>();  // the response that should be sent back to the client
        Page<Album> pageWithAlbums;  // the page object with the albums
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);  // instantiates the Pageable object

        // retrieves albums in the given page
        pageWithAlbums = albumRepository.findByTitleContaining(title, pageable);

        // mounts the response and return it
        response.put("items", pageWithAlbums.getContent());
        response.put("totalItems", pageWithAlbums.getTotalElements());
        response.put("totalPages", pageWithAlbums.getTotalPages());

        return response;
    }
}
