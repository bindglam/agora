package com.bindglam.agora.controller;

import com.bindglam.agora.service.UserService;
import com.bindglam.agora.user.UserDTO;
import com.bindglam.agora.user.UserPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-api")
@RequiredArgsConstructor
public final class UserController {
    private final UserService userService;

    @GetMapping(value = "/user")
    public ResponseEntity<UserDTO> getUser(@RequestParam String nickname, @RequestParam String password) {
        return ResponseEntity.of(userService.getUser(nickname, password));
    }

    @PostMapping(value = "/user")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        if(userDTO.getPermission().hasPermission(UserPermission.ADMIN)) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "You can't create an admin user.")).build();
        }

        return userService.registerUser(userDTO).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "User already exists")).build());
    }
}
