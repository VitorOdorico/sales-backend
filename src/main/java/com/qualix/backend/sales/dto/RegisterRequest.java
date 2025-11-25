package com.qualix.backend.sales.dto;

import com.qualix.backend.sales.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private Set<Role> roles;
}
