package com.tech.project.mapper;

import com.tech.project.entity.CategoryEntity;
import com.tech.project.response.CategoryInfoResponse;

public class CategoryMapper {

    public static CategoryInfoResponse toCategoryDTO(CategoryEntity entity) {
        CategoryInfoResponse response = new CategoryInfoResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setRegisterDate(entity.getRegisterDate());

        if(entity.getAdmin() != null) {
            response.setAdmin(entity.getAdmin());
        }

        return response;
    }
}
