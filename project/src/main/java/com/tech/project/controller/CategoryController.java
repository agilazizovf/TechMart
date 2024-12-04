package com.tech.project.controller;

import com.tech.project.request.CategoryRequest;
import com.tech.project.response.CategoryInfoResponse;
import com.tech.project.response.MessageResponse;
import com.tech.project.response.PageResponse;
import com.tech.project.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> create(@RequestBody @Valid CategoryRequest request) {
        return categoryService.create(request);
    }

    @GetMapping("/get-all")
    public PageResponse<CategoryInfoResponse> getAllCategories(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        return categoryService.getCategories(page, size);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryInfoResponse> findCategoryById(@PathVariable Long categoryId) {
        return categoryService.findCategoryById(categoryId);
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<MessageResponse> update(@PathVariable Long categoryId, @RequestBody @Valid
                                                  CategoryRequest request) throws IllegalAccessException {
        return categoryService.update(categoryId, request);
    }

    @DeleteMapping("/delete/{categoryId}")
    public void delete(@PathVariable Long categoryId) throws IllegalAccessException {
        categoryService.delete(categoryId);
    }
}
