package com.aptech.ministore.jwt.impl;

import com.aptech.ministore.dto.AuthResponse;
import com.aptech.ministore.dto.UserInfoDTO;
import com.aptech.ministore.entity.User;
import com.aptech.ministore.exception.BaseException;
import com.aptech.ministore.jwt.JwtConfig;
import com.aptech.ministore.jwt.JwtService;
import com.aptech.ministore.mapping.UserResponseMapping;
import com.aptech.ministore.security.UserDetailsCustom;
import com.aptech.ministore.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.Key;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final JwtConfig jwtConfig;

    private final UserDetailsService userDetailsService;

    private  final UserService userService;

    private  final UserResponseMapping userResponseMapping;

    public JwtServiceImpl(JwtConfig jwtConfig, UserDetailsService userDetailsService, UserService userService, UserResponseMapping userResponseMapping) {
        this.jwtConfig = jwtConfig;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.userResponseMapping = userResponseMapping;
    }


    @Override
    public Claims extractClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Key getKey() {
        byte[] key = Decoders.BASE64.decode(jwtConfig.getSecret());
        return Keys.hmacShaKeyFor(key);
    }

    @Override
    public AuthResponse generateToken(UserDetailsCustom userDetailsCustom) {

        Instant now = Instant.now();

        List<String> roles = new ArrayList<>();

        userDetailsCustom.getAuthorities().forEach(role -> {
            roles.add(role.getAuthority());
        });

        log.info("Roles: {} ", roles);

        //get info user
        Optional<User> user = userService.getUser(userDetailsCustom.getUsername());
        var userInfoDTO = new UserInfoDTO() ;
        if(user.isPresent()){
            userInfoDTO = userResponseMapping.mapToUserInfo(user.get());
        }
        String accessToken =  Jwts.builder()
                .setSubject(userDetailsCustom.getUsername())
                .claim("authorities", userDetailsCustom.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .claim("roles", roles)
                .claim("isEnable", userDetailsCustom.isEnabled())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(jwtConfig.getExpiration())))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        return AuthResponse.builder().accessToken(accessToken).userDTO(userInfoDTO).build();
    }

    @Override
    public boolean isValidToken(String token) {
        final String username = extractUsername(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return !ObjectUtils.isEmpty(userDetails);
    }

    private String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        Claims claims = null;

        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "Token expiration");
        } catch (UnsupportedJwtException e) {
            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "Token's not supported");
        } catch (MalformedJwtException e) {
            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "Invalid format 3 part of token");
        } catch (SignatureException e) {
            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "Invalid format token");
        } catch (Exception e) {
            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()), e.getLocalizedMessage());
        }

        return claims;
    }


}
