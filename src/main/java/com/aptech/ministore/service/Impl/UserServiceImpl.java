package com.aptech.ministore.service.Impl;

import com.aptech.ministore.dto.BaseResponseDTO;
import com.aptech.ministore.dto.UserDTO;
import com.aptech.ministore.entity.User;
import com.aptech.ministore.exception.BaseException;
import com.aptech.ministore.repository.UserRepository;
import com.aptech.ministore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public BaseResponseDTO registerAccount(UserDTO userDTO) {
        BaseResponseDTO response = new BaseResponseDTO();

        validateAccount(userDTO);

        User user = insertUser(userDTO);

        try {
            userRepository.save(user);
            response.setCode(String.valueOf(HttpStatus.OK.value()));
            response.setMessage("Create account successfully");
        } catch (Exception e) {
            response.setCode(String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()));
            response.setMessage("Service unavailable");
        }
        return response;
    }

    @Override
    public Optional<User> getUser(String email) {
        return userRepository.findByEmail(email);
    }

    private User insertUser(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        if(userDTO.getRole().isEmpty()){
            user.setRoles("USER");
        }
        user.setRoles(userDTO.getRole());

        return user;
    }

    private void validateAccount(UserDTO userDTO) {
        //validate null data
        if (ObjectUtils.isEmpty(userDTO)) {
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Data must not empty");
        }

        //validate duplicate username
        Optional<User> user = userRepository.findByEmail(userDTO.getUsername());
        if (!ObjectUtils.isEmpty(user)) {
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Username had existed");
        }

    }
}
