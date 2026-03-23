package com.bindglam.agora.user.repository;

import com.bindglam.agora.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}

