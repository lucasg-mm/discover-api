package com.discover.discoverapi.controllers;

import com.discover.discoverapi.entities.AppUser;
import com.discover.discoverapi.services.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@Tag(name = "Authentication controller. These endpoints are meant to be used in login situations.")
public class AuthController {
    @Autowired
    AppUserService userService;

    @Operation(description = "Gets the logged user. It's used for login.")
    @GetMapping("/login")
    public Principal getUser(Principal user){
        return user;
    }

    @Operation(description = "Registers a new user.")
    @PostMapping("/register")
    public ResponseEntity<AppUser> registerUser(@RequestBody Map<String, String> newUser){
        AppUser registeredUser = userService.registerNormalUser(newUser.get("username"), newUser.get("password"));
        return ResponseEntity.ok(registeredUser);
    }
}
