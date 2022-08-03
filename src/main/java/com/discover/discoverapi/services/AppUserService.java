package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.AppUser;
import com.discover.discoverapi.entities.SecurityAppUser;
import com.discover.discoverapi.repositories.AppUserRepository;
import com.discover.discoverapi.services.exceptions.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService implements UserDetailsService {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

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

    // registers a normal user
    // (doesn't have admin privileges)
    public AppUser registerNormalUser(String username, String password){
        if (!checkIfUserExists(username)) { // if the user doesn't exist, registers him
            String hashedPassword = passwordEncoder.encode(password);
            AppUser newUser = new AppUser(username, hashedPassword, "NORMAL");
            return appUserRepository.save(newUser);
        }
        else{ // if the user exist, throws exception
            throw new UserAlreadyExistsException("A user with this username already exists!");
        }
    }

    // checks if a user already exists in the database
    public boolean checkIfUserExists(String username){
        try {
            loadUserByUsername(username);
            return true;
        }
        catch (UsernameNotFoundException e){
            return false;
        }
    }
}
