package com.aptech.ministore.security;

import com.aptech.ministore.repository.UserRepository;
import com.aptech.ministore.security.UserDetailsCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceCustom implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(UserDetailsCustom::new)
                .orElseThrow(() -> new UsernameNotFoundException("No user found"));
    }
}
