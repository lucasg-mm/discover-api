package com.discover.discoverapi.controllers;

import com.discover.discoverapi.entities.AppUser;
import com.discover.discoverapi.services.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@Tag(name = "User controller. These endpoints are meant to be used in login and logout situations.")
public class UserController {
    @Autowired
    AppUserService userService;

    @Operation(description = "Gets the logged user. It's used for login.")
    @RequestMapping("/user")
    public Principal getUser(Principal user){
        return user;
    }

    @Operation(description = "Registers a new user.")
    @RequestMapping("/register")
    public ResponseEntity<AppUser> registerUser(Map<String, String> newUser){
        AppUser registeredUser = userService.registerNormalUser(newUser.get("username"), newUser.get("password"));
        return ResponseEntity.ok(registeredUser);
    }
}
