package com.aptech.ministore.dto;

import com.aptech.ministore.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({"user", "accessToken"})
public class AuthResponse {
    @JsonProperty("user")
    private UserInfoDTO userDTO;
    private String accessToken;
}
