package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.AppUser;
import com.discover.discoverapi.repositories.AppUserRepository;
import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AlbumService albumService;

    public AppUser findUserByUsername(String username) {
        // gets a list of users with the provided username
        List<AppUser> foundAppUsers = appUserRepository.findByUsername(username);

        // if the list is empty, there is no user with that username
        if (foundAppUsers.size() == 0){
            throw new ObjectNotFoundException("User with this username not found!");
        }

        return foundAppUsers.get(0);
    }

    public Set<Album> getLikedAlbums(String username){
        AppUser foundUser = findUserByUsername(username);
        return foundUser.getLikedAlbums();
    }

    public Album addAlbumToLiked(String username, long albumId) {
        appUserRepository.addAlbumToLiked(username, albumId);
        return albumService.findById(albumId);
    }

    public void removeAlbumFromLiked(String username, long albumId) {
        appUserRepository.removeAlbumFromLiked(username, albumId);
    }
}
