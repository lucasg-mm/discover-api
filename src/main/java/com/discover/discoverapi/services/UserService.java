package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.AppUser;
import com.discover.discoverapi.repositories.AppUserRepository;
import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private AppUserRepository appUserRepository;

    public AppUser findUserById(long userId) {
        return appUserRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User of id " + userId + " not found."));
    }

    public Set<Album> getLikedAlbums(long userId){
        AppUser foundUser = findUserById(userId);
        return foundUser.getLikedAlbums();
    }
}
