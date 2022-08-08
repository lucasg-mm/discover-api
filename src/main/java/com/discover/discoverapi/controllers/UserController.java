package com.discover.discoverapi.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/users")
@AllArgsConstructor
@Tag(name = "User Controller")
public class UserController {
    // gets a user's list of liked albums

    // gets a user's list of liked tracks

    // gets a user's list of liked artists

    // gets a user's list of liked genres
}
