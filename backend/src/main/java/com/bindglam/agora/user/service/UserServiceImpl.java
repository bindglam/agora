package com.bindglam.agora.user.service;

import com.bindglam.agora.user.*;
import com.bindglam.agora.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public final class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<DefaultUserDTO> getUserInternal(@NonNull UUID key) {
        return userRepository.findById(key).map(User::toDTO);
    }

    @Override
    public Optional<SecureUserDTO> getUser(@NonNull UUID key) {
        return getUserInternal(key).map(DefaultUserDTO::secure);
    }

    @Override
    public UserRequests.Create.@NonNull Response createUser(UserRequests.@NonNull Create request) {
        if(userRepository.findByUsername(request.username()).isPresent()) {
            return UserRequests.Create.Response.builder().errorMessage("동일한 계정이 이미 존재합니다.").build();
        }

        return UserRequests.Create.Response.builder()
                .user(userRepository.save(
                        User.builder()
                                .username(request.username())
                                .permission(UserPermission.DEFAULT)
                                .createdAt(LocalDateTime.now())
                                .build()
                ).toDTO())
                .build();
    }
}
