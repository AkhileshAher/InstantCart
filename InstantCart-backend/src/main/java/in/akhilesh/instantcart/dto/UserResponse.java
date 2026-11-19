package in.akhilesh.instantcart.dto;

import in.akhilesh.instantcart.entity.User;
import in.akhilesh.instantcart.entity.UserRole;

public record UserResponse(Long id, String username, String email, String phone, UserRole role) {
        public static UserResponse from(User u) {
            return new UserResponse(u.getId(), u.getUsername(), u.getEmail(), u.getPhone(), u.getRole());
        }
    }