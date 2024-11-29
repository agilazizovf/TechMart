package com.tech.project.service.impl;

import com.tech.project.entity.AdminEntity;
import com.tech.project.entity.AuthorityEntity;
import com.tech.project.entity.UserEntity;
import com.tech.project.exception.AlreadyExistsException;
import com.tech.project.exception.ResourceNotFoundException;
import com.tech.project.repository.AdminRepository;
import com.tech.project.repository.UserRepository;
import com.tech.project.request.AdminRequest;
import com.tech.project.request.LoginRequest;
import com.tech.project.response.JwtAuthenticationResponse;
import com.tech.project.response.MessageResponse;
import com.tech.project.service.AdminService;
import com.tech.project.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public ResponseEntity<MessageResponse> register(AdminRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AlreadyExistsException("User already exists");
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        AuthorityEntity authority = new AuthorityEntity("ROLE_ADMIN");
        Set<AuthorityEntity> authorityEntitySet = Set.of(authority);
        user.setAuthorities(authorityEntitySet);

        userRepository.save(user);

        AdminEntity admin = new AdminEntity();
        admin.setName(request.getName());
        admin.setSurname(request.getSurname());
        admin.setUsername(request.getUsername());
        admin.setPhone(request.getPhone());
        admin.setEmail(request.getEmail());
        admin.setRegisterDate(LocalDate.now());
        admin.setUpdateDate(LocalDate.now());
        admin.setUser(user);

        MessageResponse response = new MessageResponse();
        response.setMessage("Created Successfully");

        adminRepository.save(admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public JwtAuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        var jwt = jwtService.generateToken(user);
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        return jwtAuthenticationResponse;
    }
}
