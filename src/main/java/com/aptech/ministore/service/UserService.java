package com.aptech.ministore.service;

import com.aptech.ministore.dto.BaseResponseDTO;
import com.aptech.ministore.dto.UserDTO;
import com.aptech.ministore.entity.User;

import java.util.Optional;

public interface UserService {
    BaseResponseDTO registerAccount(UserDTO userDTO);

    Optional<User> getUser (String email);
}
