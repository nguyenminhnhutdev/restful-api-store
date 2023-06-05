package com.aptech.ministore.config;

import com.aptech.ministore.config.filter.CustomAuthenticationProvider;
import com.aptech.ministore.security.UserDetailsServiceCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final CustomAuthenticationProvider customAuthenticationProvider;
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsServiceCustom();
    }

}
