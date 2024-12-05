package com.tech.project.response;

import com.tech.project.entity.CategoryEntity;
import com.tech.project.entity.UserEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductInfoResponse {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime registerDate;
    private CategoryEntity category;
    private UserEntity user;
}
