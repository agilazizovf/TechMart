package com.tech.project.service.impl;

import com.tech.project.ExceptionDTO;
import com.tech.project.entity.AdminEntity;
import com.tech.project.entity.AuthorityEntity;
import com.tech.project.entity.UserEntity;
import com.tech.project.exception.AlreadyExistsException;
import com.tech.project.repository.AdminRepository;
import com.tech.project.repository.UserRepository;
import com.tech.project.request.AdminRequest;
import com.tech.project.request.LoginRequest;
import com.tech.project.response.LoginResponse;
import com.tech.project.response.MessageResponse;
import com.tech.project.service.AdminService;
import com.tech.project.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public ResponseEntity<MessageResponse> register(AdminRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AlreadyExistsException("User already exists");
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        AuthorityEntity authority = new AuthorityEntity("ADMIN");
        Set<AuthorityEntity> authorityEntitySet = Set.of(authority);
        user.setAuthorities(authorityEntitySet);

        userRepository.save(user);

        AdminEntity admin = new AdminEntity();
        modelMapper.map(request, admin);
        admin.setRegisterDate(LocalDate.now());
        admin.setUpdateDate(LocalDate.now());
        admin.setUser(user);

        MessageResponse response = new MessageResponse();
        response.setMessage("Created Successfully");

        adminRepository.save(admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<?> login(LoginRequest loginReq){
        log.info("authenticate method started by: {}", loginReq.getUsername());
        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getUsername(),
                            loginReq.getPassword()));
            log.info("authentication details: {}", authentication);
            String username = authentication.getName();
            UserEntity client = new UserEntity(username,"");
            String token = jwtUtil.createToken(client);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            LoginResponse loginRes = new LoginResponse(username,token);
            log.info("user: {} logged in",  client.getUsername());
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(loginRes);

        }catch (BadCredentialsException e){
            ExceptionDTO exceptionDTO = new ExceptionDTO(HttpStatus.BAD_REQUEST.value(),"Invalid username or password");
            log.error("Error due to {} ", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);
        }catch (Exception e){
            ExceptionDTO exceptionDTO = new ExceptionDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            log.error("Error due to {} ", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);
        }
    }
}
