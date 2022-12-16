package com.example.FileApp.controllers;

import com.example.FileApp.exceptions.UserAlreadyExistsException;
import com.example.FileApp.services.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUser user)
            throws UserAlreadyExistsException
    {
        userService.registerUser(user.getUsername(), user.getPassword());
        return ResponseEntity.ok("User " + user.getUsername()+ " is now registered!");
    }


    @Getter
    @Setter
    public static class RegisterUser {
        private String username;
        private String password;
    }

}
