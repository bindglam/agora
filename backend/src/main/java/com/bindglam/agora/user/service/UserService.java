package com.bindglam.agora.user.service;

import com.bindglam.agora.user.DefaultUserDTO;
import com.bindglam.agora.user.UserRequests;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<DefaultUserDTO> getUser(@NonNull UUID key);

    Optional<DefaultUserDTO> getUser(@NonNull String username);

    UserRequests.Create.@NonNull Response createUser(UserRequests.@NonNull Create request);
}
