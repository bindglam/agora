package com.bindglam.agora.service;

import com.bindglam.agora.user.UserDTO;

import java.util.Optional;

public interface UserService {
    Optional<UserDTO> getUser(String nickname, String password);

    Optional<UserDTO> registerUser(UserDTO user);
}
