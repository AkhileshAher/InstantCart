package in.akhilesh.instantcart.dto;

import in.akhilesh.instantcart.entity.Category;

public record CategoryResponse(Long id, String name, String image) {
        public static CategoryResponse from(Category c) {
            return new CategoryResponse(c.getId(), c.getName(), c.getImage());
        }
    }