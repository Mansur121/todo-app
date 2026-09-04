package com.mansur.todo.service;

import com.mansur.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        // Fetch user from DB
        com.mansur.todo.entity.User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username));

        // Convert to Spring's UserDetails
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())   // "USER" → "ROLE_USER" internally
                .build();
    }
}
