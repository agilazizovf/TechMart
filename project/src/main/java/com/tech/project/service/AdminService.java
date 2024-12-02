package com.tech.project.service;

import com.tech.project.request.AdminRequest;
import com.tech.project.response.MessageResponse;
import org.springframework.http.ResponseEntity;

public interface AdminService {

    ResponseEntity<MessageResponse> register(AdminRequest request);
}
