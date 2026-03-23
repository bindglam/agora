package com.bindglam.agora.user.controller;

import com.bindglam.agora.user.UserDTO;
import com.bindglam.agora.user.UserRequests;
import com.bindglam.agora.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-api")
@RequiredArgsConstructor
public final class UserController {
    private final UserService userService;

    @GetMapping(value = "/user/{username}")
    public ResponseEntity<UserDTO> getPost(@PathVariable String username) {
        return ResponseEntity.of(userService.getUser(username));
    }

    @PostMapping(value = "/user")
    public ResponseEntity<UserRequests.Create.Response> createUser(@RequestBody UserRequests.Create request) {
        return ResponseEntity.ok(userService.createUser(request));
    }
}
