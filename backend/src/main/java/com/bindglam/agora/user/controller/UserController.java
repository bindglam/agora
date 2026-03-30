package com.bindglam.agora.user.controller;

import com.bindglam.agora.user.SecureUserDTO;
import com.bindglam.agora.user.UserRequests;
import com.bindglam.agora.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user-api")
@RequiredArgsConstructor
public final class UserController {
    private final UserService userService;

    @GetMapping(value = "/user/{key}")
    public ResponseEntity<SecureUserDTO> getUser(@PathVariable UUID key) {
        return ResponseEntity.of(userService.getUser(key));
    }

    @PostMapping(value = "/user")
    public ResponseEntity<UserRequests.Create.Response> createUser(@RequestBody UserRequests.Create request) {
        return ResponseEntity.ok(userService.createUser(request));
    }
}
