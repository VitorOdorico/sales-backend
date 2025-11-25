package com.qualix.backend.sales.services;

import com.qualix.backend.sales.dto.RegisterRequest;
import com.qualix.backend.sales.entities.User;

import com.qualix.backend.sales.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User register(RegisterRequest req) {

        if (repository.findByUsername(req.getUsername()).isPresent()) {
            throw new RuntimeException("Usuário já existe");
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        if (req.getRole() != null) {
            user.setRole(req.getRole());
        }

        return repository.save(user);
    }
}
