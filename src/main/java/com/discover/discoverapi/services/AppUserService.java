package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.AppUser;
import com.discover.discoverapi.entities.SecurityAppUser;
import com.discover.discoverapi.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService implements UserDetailsService {
    @Autowired
    private AppUserRepository appUserRepository;

    // gets a user by its username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // gets a list of users with the provided username
        List<AppUser> foundAppUsers = appUserRepository.findByUsername(username);

        // if the list is empty, there is no user with that username
        if (foundAppUsers.size() == 0){
            throw new UsernameNotFoundException("Incorrect username!");
        }

        // returns the user as UserDetails implementation
        return new SecurityAppUser(foundAppUsers.get(0));
    }
}
