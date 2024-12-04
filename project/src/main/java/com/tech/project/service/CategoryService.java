package com.tech.project.service;

import com.tech.project.entity.UserEntity;
import com.tech.project.request.CategoryRequest;
import com.tech.project.response.CategoryInfoResponse;
import com.tech.project.response.MessageResponse;
import com.tech.project.response.PageResponse;
import org.springframework.http.ResponseEntity;

public interface CategoryService {

    ResponseEntity<MessageResponse> create(CategoryRequest request);
    PageResponse<CategoryInfoResponse> getCategories(int page, int size);
    ResponseEntity<CategoryInfoResponse> findCategoryById(Long categoryId);
    ResponseEntity<MessageResponse> update(Long categoryId, CategoryRequest request) throws IllegalAccessException;
    void delete(Long categoryId) throws IllegalAccessException;
    UserEntity getCurrentUser();
}
