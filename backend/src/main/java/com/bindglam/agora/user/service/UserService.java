package com.bindglam.agora.user.service;

import com.bindglam.agora.user.UserDTO;
import com.bindglam.agora.user.UserRequests;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public interface UserService {
    Optional<UserDTO> getUser(@NonNull String username);

    UserRequests.Create.@NonNull Response createUser(UserRequests.@NonNull Create request);
}
