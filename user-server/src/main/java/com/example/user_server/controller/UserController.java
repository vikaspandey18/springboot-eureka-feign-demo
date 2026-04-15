package com.example.user_server.controller;

import com.example.user_server.dto.UserResponse;
import com.example.user_server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUser(){
        List<UserResponse> userResponse = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        UserResponse userResponse = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserResponse userResponse){
        UserResponse savedUserResponse = userService.createUser(userResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserResponse);
    }


}
