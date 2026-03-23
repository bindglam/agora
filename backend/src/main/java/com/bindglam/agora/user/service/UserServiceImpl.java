package com.bindglam.agora.user.service;

import com.bindglam.agora.user.User;
import com.bindglam.agora.user.UserDTO;
import com.bindglam.agora.user.UserRequests;
import com.bindglam.agora.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<UserDTO> getUser(@NonNull String username) {
        return userRepository.findById(username).map(User::toDTO);
    }

    @Override
    public UserRequests.Create.@NonNull Response createUser(UserRequests.@NonNull Create request) {
        if(getUser(request.username()).isPresent()) {
            return UserRequests.Create.Response.builder().errorMessage("동일한 계정이 이미 존재합니다.").build();
        }

        return UserRequests.Create.Response.builder()
                .user(userRepository.save(
                        User.builder()
                                .username(request.username())
                                .build()
                ).toDTO())
                .build();
    }
}
