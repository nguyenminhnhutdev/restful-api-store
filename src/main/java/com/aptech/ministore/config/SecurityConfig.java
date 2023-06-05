package com.aptech.ministore.config;

import com.aptech.ministore.config.filter.CustomAuthenticationProvider;
import com.aptech.ministore.config.filter.JwtTokenAuthenticationFilter;
import com.aptech.ministore.config.filter.JwtUsernamePasswordAuthenticationFilter;
import com.aptech.ministore.exception.CustomAccessDeniedHandler;
import com.aptech.ministore.jwt.JwtConfig;
import com.aptech.ministore.jwt.JwtService;
import com.aptech.ministore.security.UserDetailsServiceCustom;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final JwtConfig jwtConfig;
    private final JwtService jwtService;
    private final UserDetailsServiceCustom userDetailsServiceCustom;
    private final BCryptPasswordEncoder passwordEncoder;
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);

        builder.userDetailsService(userDetailsServiceCustom).passwordEncoder(passwordEncoder);

        return builder.build();
    }
    @Autowired
    public void configGlobal(final AuthenticationManagerBuilder auth){
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
//
//        builder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
//
//        AuthenticationManager manager = builder.build();

        http
                .cors().disable()
                .csrf().disable()
                .formLogin().disable()
                .authorizeHttpRequests()
                .requestMatchers("/account/**").permitAll()
                .requestMatchers("/category/**").permitAll()
                .requestMatchers("/guest/**").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/user/**").hasAuthority("USER")
                .anyRequest().authenticated()
                .and()
                .authenticationManager(authenticationManager(http))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(
                        ((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                )
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                //.addFilterBefore(new JwtUsernamePasswordAuthenticationFilter(manager, jwtConfig, jwtService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtUsernamePasswordAuthenticationFilter(authenticationManager(http), jwtConfig, jwtService), UsernamePasswordAuthenticationFilter.class)
                //.addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig, jwtService), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }
}