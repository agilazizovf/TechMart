package com.tech.project.mapper;

import com.tech.project.entity.ProductEntity;
import com.tech.project.response.ProductInfoResponse;

public class ProductMapper {

    public static ProductInfoResponse toProductDTO(ProductEntity product) {
        ProductInfoResponse response = new ProductInfoResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setRegisterDate(product.getRegisterDate());

        if(product.getCategory() != null) {
           response.setCategory(product.getCategory());
        }

        if(product.getUser() != null) {
            response.setUser(product.getUser());
        }

        return response;
    }
}
