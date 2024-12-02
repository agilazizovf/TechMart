package com.tech.project.service;


import com.tech.project.entity.AuthorityEntity;
import com.tech.project.entity.UserEntity;
import com.tech.project.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository clientRepository;

    public CustomUserDetailsService(UserRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity client = clientRepository.findByUsername(username).orElseThrow();
        List<String> roles = new ArrayList<>();
        Set<AuthorityEntity> authorities = client.getAuthorities();
        for (AuthorityEntity authority : authorities) {
            roles.add(authority.getName());
        }
        UserDetails userDetails;
        userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(client.getUsername())
                .password(client.getPassword())
                .roles(roles.toArray(new String[0]))
                .build();
        return userDetails;
    }
}
