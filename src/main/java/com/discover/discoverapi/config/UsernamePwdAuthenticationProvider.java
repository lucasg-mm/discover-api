package com.discover.discoverapi.config;

import com.discover.discoverapi.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsernamePwdAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private AuthService appUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // contains the authentication logic
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // gets the username and password that the user provided
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        // gets the user with the provided username
        UserDetails user = appUserService.loadUserByUsername(username);

        // if the passwords match...
        if (passwordEncoder.matches(password, user.getPassword())){
            // return the auth token
            return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
        }
        else{
            // if they don't match, there is a problem!
            throw new BadCredentialsException("Incorrect password!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
