package com.bindglam.agora.service;

import com.bindglam.agora.user.User;
import com.bindglam.agora.user.UserDTO;
import com.bindglam.agora.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<UserDTO> getUser(String nickname, String password) {
        return userRepository.findByNicknameAndPassword(nickname, password).map(User::toDTO);
    }

    @Override
    public Optional<UserDTO> registerUser(UserDTO user) {
        if (userRepository.findById(user.getNickname()).isPresent())
            return Optional.empty();
        return Optional.of(userRepository.save(user.toEntity()).toDTO());
    }
}
