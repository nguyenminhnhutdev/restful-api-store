package com.aptech.ministore.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDTO {
    private String name;
    private String email;
    private String phone;
    private String dateOfBirth;
    private String gender;
    private String image;
}
