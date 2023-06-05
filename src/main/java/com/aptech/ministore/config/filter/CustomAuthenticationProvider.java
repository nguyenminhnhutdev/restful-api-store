package com.aptech.ministore.config.filter;

import com.aptech.ministore.entity.User;
import com.aptech.ministore.exception.BaseException;
import com.aptech.ministore.repository.UserRepository;
import jdk.dynalink.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("Start actual authentication");
        final String username = authentication.getName();

        final String password = authentication.getCredentials().toString();

        Optional<User> user;
        try {
            user = userRepository.findByEmail(username);
        }catch (Exception e){
            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "User's not found");
        }

        final List<GrantedAuthority> authorities = getAuthorities(user.get());

        final Authentication auth = new UsernamePasswordAuthenticationToken(username, password, authorities);

        log.info("End actual authentication");
        return auth;
    }

    private List<GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> result = Arrays.stream(user.getRoles()
                        .split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return result;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}