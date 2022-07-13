package com.discover.discoverapi.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Tag(name = "User Controller")
public class UserController {

    @GetMapping(value = "", produces = "application/json")
    public Principal getLoggedUser(Principal user){
        return user;
    }
}
