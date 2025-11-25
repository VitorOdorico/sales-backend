package com.qualix.backend.sales.entities;
import com.qualix.backend.sales.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TB_User")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING) // Salva como texto no banco
    private Role role = Role.USER;
}
