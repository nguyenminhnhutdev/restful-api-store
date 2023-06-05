package com.aptech.ministore.jwt;

import com.aptech.ministore.dto.AuthResponse;
import com.aptech.ministore.security.UserDetailsCustom;
import io.jsonwebtoken.Claims;

import java.security.Key;

public interface JwtService {
    Claims extractClaims(String token);

    Key getKey();

    AuthResponse generateToken(UserDetailsCustom userDetailsCustom);

    boolean isValidToken(String token);
}
