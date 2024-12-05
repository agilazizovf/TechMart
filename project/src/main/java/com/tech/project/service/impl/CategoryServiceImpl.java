package com.tech.project.service.impl;

import com.tech.project.entity.CategoryEntity;
import com.tech.project.entity.UserEntity;
import com.tech.project.exception.ResourceNotFoundException;
import com.tech.project.mapper.CategoryMapper;
import com.tech.project.repository.CategoryRepository;
import com.tech.project.repository.UserRepository;
import com.tech.project.request.CategoryRequest;
import com.tech.project.response.CategoryInfoResponse;
import com.tech.project.response.MessageResponse;
import com.tech.project.response.PageResponse;
import com.tech.project.service.CategoryService;
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
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<MessageResponse> create(CategoryRequest request) {
        UserEntity user = getCurrentUser();

        CategoryEntity category = new CategoryEntity();
        modelMapper.map(request, category);
        category.setRegisterDate(LocalDateTime.now());
        category.setUpdateDate(LocalDateTime.now());
        category.setAdmin(user.getAdmin());

        categoryRepository.save(category);

        MessageResponse response = new MessageResponse();
        response.setMessage("Created Successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public PageResponse<CategoryInfoResponse> getCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryEntity> categoryEntities = categoryRepository.findAll(pageable);

        List<CategoryInfoResponse> categoryInfoResponses = categoryEntities
                .stream()
                .map(CategoryMapper::toCategoryDTO)
                .collect(Collectors.toList());

        PageResponse<CategoryInfoResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(categoryInfoResponses);
        pageResponse.setPage(categoryEntities.getPageable().getPageNumber());
        pageResponse.setSize(categoryEntities.getPageable().getPageSize());
        pageResponse.setTotalElements(categoryEntities.getTotalElements());
        pageResponse.setTotalPages(categoryEntities.getTotalPages());
        pageResponse.setLast(categoryEntities.isLast());
        pageResponse.setFirst(categoryEntities.isFirst());

        return pageResponse;
    }

    @Override
    public ResponseEntity<CategoryInfoResponse> findCategoryById(Long categoryId) {
        UserEntity user = getCurrentUser();
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        CategoryInfoResponse response = new CategoryInfoResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setRegisterDate(category.getRegisterDate());
        response.setAdmin(user.getAdmin());

        return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }

    @Override
    public ResponseEntity<MessageResponse> update(Long categoryId, CategoryRequest request) throws IllegalAccessException {
        UserEntity user = getCurrentUser();
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if(!category.getAdmin().getUser().equals(user)) {
            throw new IllegalAccessException("You do not have permission to update this category");
        }
        modelMapper.map(request, category);
        category.setUpdateDate(LocalDateTime.now());
        categoryRepository.save(category);

        MessageResponse response = new MessageResponse();
        response.setMessage("Updated Successfully");

        return ResponseEntity.ok(response);
    }

    @Override
    public void delete(Long categoryId) throws IllegalAccessException {
        UserEntity user = getCurrentUser();
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if(!category.getAdmin().getUser().equals(user)) {
            throw new IllegalAccessException("You do not have permission to delete this category");
        }

        categoryRepository.deleteById(categoryId);
    }

    @Override
    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
