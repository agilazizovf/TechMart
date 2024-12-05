package com.tech.project.controller;

import com.tech.project.request.ProductRequest;
import com.tech.project.response.MessageResponse;
import com.tech.project.response.PageResponse;
import com.tech.project.response.ProductInfoResponse;
import com.tech.project.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create/{categoryId}")
    public ResponseEntity<MessageResponse> create(@PathVariable Long categoryId, @RequestBody @Valid ProductRequest request) {
        return productService.create(categoryId, request);
    }

    @GetMapping("/get-all")
    public PageResponse<ProductInfoResponse> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        return productService.getProducts(page, size);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductInfoResponse> findProductById(@PathVariable Long productId) {
        return productService.findProductById(productId);
    }

    @PutMapping("/update/{categoryId}/{productId}")
    public ResponseEntity<MessageResponse> update(@PathVariable Long categoryId,
                                                  @PathVariable Long productId,
                                                  @RequestBody @Valid ProductRequest request) throws IllegalAccessException {
        return productService.update(categoryId, productId, request);
    }

    @DeleteMapping("/delete/{productId}")
    public void delete(@PathVariable Long productId) throws IllegalAccessException {
        productService.delete(productId);
    }
}
