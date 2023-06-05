package com.aptech.ministore.mapping;

import com.aptech.ministore.dto.UserInfoDTO;
import com.aptech.ministore.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapping {
    public UserInfoDTO mapToUserInfo(User user){
       return UserInfoDTO.builder()
                .name(user.getName())
                .phone(user.getName())
                .email(user.getEmail())
                .image(user.getImage())
                .gender(user.getGender())
                .build();
    }

}
