package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.repositories.AlbumRepository;
import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import com.discover.discoverapi.services.fileupload.Uploader;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class AlbumService {
    private AlbumRepository albumRepository;
    private TrackService trackService;
    private Uploader imageUploader;

    // find all
    @Transactional
    public List<Album> findAll(){
        return albumRepository.findAll();
    }

    // find by id
    @Transactional
    public Album findById(long id){
        return albumRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Album of id " + id + " not found."));
    }

    // update by id
    @Transactional
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
    @Transactional
    public Album create(Album album){
        album.setId(0);  // shouldn't be null?
        return albumRepository.save(album);
    }

    // delete by id
    @Transactional
    public void deleteById(long id){
        if (albumRepository.existsById(id)){
            albumRepository.deleteById(id);
        }
        else{
            throw new ObjectNotFoundException("Album of id " + id + " not found.");
        }
    }

    // find all the album's tracks
    @Transactional
    public Set<Track> findAllTracksOfAlbum(long albumId){
        Album foundAlbum = findById(albumId);
        return foundAlbum.getTracks();
    }

    // add track to an album's list of tracks
    @Transactional
    public Track addTrackToAlbum(long albumId, long trackId){
        // finds the album by id
        Album foundAlbum = findById(albumId);

        // finds the track by id
        Track foundTrack = trackService.findById(trackId);

        // gets album's tracks
        foundAlbum.getTracks().add(foundTrack);
        albumRepository.save(foundAlbum);

        return foundTrack;
    }

    // delete a track from the list of tracks from an album
    @Transactional
    public void deleteTrackFromAlbum(long albumId, long trackId){
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
    public void setAlbumCover(long albumId, MultipartFile file){
        imageUploader.upload(file, "album-covers");
    }
}
