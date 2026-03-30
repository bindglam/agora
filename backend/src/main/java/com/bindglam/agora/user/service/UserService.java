package com.bindglam.agora.user.service;

import com.bindglam.agora.user.DefaultUserDTO;
import com.bindglam.agora.user.SecureUserDTO;
import com.bindglam.agora.user.UserRequests;
import org.hibernate.Internal;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    @Internal
    Optional<DefaultUserDTO> getUserInternal(@NonNull UUID key);

    Optional<SecureUserDTO> getUser(@NonNull UUID key);

    UserRequests.Create.@NonNull Response createUser(UserRequests.@NonNull Create request);
}
