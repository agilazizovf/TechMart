package com.tech.project.service;

import com.tech.project.entity.UserEntity;
import com.tech.project.request.ProductRequest;
import com.tech.project.response.MessageResponse;
import com.tech.project.response.PageResponse;
import com.tech.project.response.ProductInfoResponse;
import org.springframework.http.ResponseEntity;

public interface ProductService {

    ResponseEntity<MessageResponse> create(Long categoryId, ProductRequest request);
    PageResponse<ProductInfoResponse> getProducts(int page, int size);
    ResponseEntity<ProductInfoResponse> findProductById(Long productId);
    ResponseEntity<MessageResponse> update(Long categoryId, Long productId, ProductRequest request) throws IllegalAccessException;
    void delete(Long productId) throws IllegalAccessException;
    UserEntity getCurrentUser();
}
