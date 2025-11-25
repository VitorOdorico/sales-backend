package com.qualix.backend.sales.dto;
import com.qualix.backend.sales.enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private Role role;
}
