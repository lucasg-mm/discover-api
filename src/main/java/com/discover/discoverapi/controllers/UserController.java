package com.discover.discoverapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@Tag(name = "User controller. These endpoints are meant to be used in login and logout situations.")
public class UserController {
    @Operation(description = "Gets the logged user. It's used for login.")
    @RequestMapping("/user")
    public Principal getUser(Principal user){
        return user;
    }
}
