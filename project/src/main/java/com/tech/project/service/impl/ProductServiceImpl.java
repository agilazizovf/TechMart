package com.tech.project.service.impl;

import com.tech.project.entity.CategoryEntity;
import com.tech.project.entity.ProductEntity;
import com.tech.project.entity.UserEntity;
import com.tech.project.exception.ResourceNotFoundException;
import com.tech.project.mapper.CategoryMapper;
import com.tech.project.mapper.ProductMapper;
import com.tech.project.repository.CategoryRepository;
import com.tech.project.repository.ProductRepository;
import com.tech.project.repository.UserRepository;
import com.tech.project.request.ProductRequest;
import com.tech.project.response.CategoryInfoResponse;
import com.tech.project.response.MessageResponse;
import com.tech.project.response.PageResponse;
import com.tech.project.response.ProductInfoResponse;
import com.tech.project.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    @Override
    public ResponseEntity<MessageResponse> create(Long categoryId, ProductRequest request) {
        UserEntity user = getCurrentUser();

        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        ProductEntity product = new ProductEntity();
        modelMapper.map(request, product);
        product.setRegisterDate(LocalDateTime.now());
        product.setUpdateDate(LocalDateTime.now());
        product.setCategory(category);
        product.setUser(user);

        productRepository.save(product);

        MessageResponse response = new MessageResponse();
        response.setMessage("Created Successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Override
    public PageResponse<ProductInfoResponse> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductEntity> productEntities = productRepository.findAll(pageable);

        List<ProductInfoResponse> productInfoResponses = productEntities
                .stream()
                .map(ProductMapper::toProductDTO)
                .collect(Collectors.toList());

        PageResponse<ProductInfoResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(productInfoResponses);
        pageResponse.setPage(productEntities.getPageable().getPageNumber());
        pageResponse.setSize(productEntities.getPageable().getPageSize());
        pageResponse.setTotalElements(productEntities.getTotalElements());
        pageResponse.setTotalPages(productEntities.getTotalPages());
        pageResponse.setLast(productEntities.isLast());
        pageResponse.setFirst(productEntities.isFirst());

        return pageResponse;
    }

    @Override
    public ResponseEntity<ProductInfoResponse> findProductById(Long productId) {
        UserEntity user = getCurrentUser();

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        ProductInfoResponse response = new ProductInfoResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setRegisterDate(product.getRegisterDate());
        response.setCategory(product.getCategory());
        response.setUser(user);

        return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }

    @Override
    public ResponseEntity<MessageResponse> update(Long categoryId, Long productId, ProductRequest request) throws IllegalAccessException {
        UserEntity user = getCurrentUser();

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!product.getUser().equals(user)) {
            throw new IllegalAccessException("You do not have permission to update this product");
        }

        modelMapper.map(request, product);
        product.setUpdateDate(LocalDateTime.now());
        product.setCategory(category);
        product.setUser(user);

        productRepository.save(product);

        MessageResponse response = new MessageResponse();
        response.setMessage("Updated Successfully");

        return ResponseEntity.ok(response);
    }

    @Override
    public void delete(Long productId) throws IllegalAccessException {
        UserEntity user = getCurrentUser();

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!product.getUser().equals(user)) {
            throw new IllegalAccessException("You do not have permission to delete this product");
        }
        productRepository.deleteById(productId);
    }


    @Override
    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
