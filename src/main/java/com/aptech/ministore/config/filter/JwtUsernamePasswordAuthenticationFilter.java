package com.aptech.ministore.config.filter;

import com.aptech.ministore.dto.AuthResponse;
import com.aptech.ministore.dto.BaseResponseDTO;
import com.aptech.ministore.dto.LoginRequest;
import com.aptech.ministore.jwt.JwtConfig;
import com.aptech.ministore.jwt.JwtService;
import com.aptech.ministore.security.UserDetailsCustom;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Collections;

@Slf4j
public class JwtUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final JwtService jwtService;

    private final ObjectMapper objectMapper;

    public static final ObjectWriter JSON_WRITER = new ObjectMapper().writer().withDefaultPrettyPrinter();

    public JwtUsernamePasswordAuthenticationFilter(AuthenticationManager manager,
                                                   JwtConfig jwtConfig,
                                                   JwtService jwtService) {
        super(new AntPathRequestMatcher(jwtConfig.getUrl(), "POST"));
        setAuthenticationManager(manager);
        this.objectMapper = new ObjectMapper();
        this.jwtService = jwtService;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        log.info("Start attempt to authentication");
        LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        log.info("End attempt to authentication");

        return getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword(),
                        Collections.emptyList()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        UserDetailsCustom userDetailsCustom = (UserDetailsCustom) authentication.getPrincipal();

        AuthResponse accessToken = jwtService.generateToken(userDetailsCustom);
        String json = JSON_WRITER.writeValueAsString(accessToken);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(json);
        log.info("End success authentication: {}", accessToken);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        BaseResponseDTO responseDTO = new BaseResponseDTO();
        responseDTO.setCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        responseDTO.setMessage(failed.getLocalizedMessage());

        String json = JSON_WRITER.writeValueAsString(responseDTO);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(json);
        return;
    }
}