package com.qualix.backend.sales.services;

import com.qualix.backend.sales.dto.RegisterRequest;
import com.qualix.backend.sales.entities.User;
import com.qualix.backend.sales.enums.Role;
import com.qualix.backend.sales.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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

        if (req.getRoles() != null && !req.getRoles().isEmpty()) {
            user.setRoles(req.getRoles());
        } else {
            user.setRoles(Set.of(Role.USER));
        }

        return repository.save(user);
    }

    public List<User> listAllUsers() {
        return repository.findAll();
    }

    public User getUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public User updateUser(Long id, RegisterRequest req) {
        User user = getUserById(id);

        if (req.getUsername() != null) user.setUsername(req.getUsername());
        if (req.getPassword() != null && !req.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(req.getPassword()));
        }
        if (req.getRoles() != null && !req.getRoles().isEmpty()) user.setRoles(req.getRoles());

        return repository.save(user);
    }

    public void deleteUser(Long id) {
        getUserById(id); // valida se existe
        repository.deleteById(id);
    }

    public boolean matchesPassword(String raw, String encoded) {
        return passwordEncoder.matches(raw, encoded);
    }

    public String encodePassword(String raw) {
        return passwordEncoder.encode(raw);
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}

