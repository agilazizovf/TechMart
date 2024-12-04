package com.tech.project.response;

import com.tech.project.entity.AdminEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryInfoResponse {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime registerDate;
    private AdminEntity admin;
}
