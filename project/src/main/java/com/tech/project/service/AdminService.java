package com.tech.project.service;

import com.tech.project.request.AdminRequest;
import com.tech.project.request.LoginRequest;
import com.tech.project.response.JwtAuthenticationResponse;
import com.tech.project.response.MessageResponse;
import org.springframework.http.ResponseEntity;

public interface AdminService {

    ResponseEntity<MessageResponse> register(AdminRequest request);

    JwtAuthenticationResponse login(LoginRequest request);
}
