package com.tech.project.controller;

import com.tech.project.request.AdminRequest;
import com.tech.project.request.LoginRequest;
import com.tech.project.response.JwtAuthenticationResponse;
import com.tech.project.response.MessageResponse;
import com.tech.project.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registration(@RequestBody @Valid AdminRequest request) {
        return adminService.register(request);
    }

    @PostMapping("/login")
    public JwtAuthenticationResponse login(@RequestBody @Valid LoginRequest request) {
        return adminService.login(request);
    }
}
