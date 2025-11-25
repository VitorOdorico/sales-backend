package com.qualix.backend.sales.controllers.Auth;

import com.qualix.backend.sales.dto.RegisterRequest;
import com.qualix.backend.sales.entities.User;
import com.qualix.backend.sales.repository.UserRepository;
import com.qualix.backend.sales.security.jwt.JwtUtil;
import com.qualix.backend.sales.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${api.auth.user}")
    private String basicUser;

    @Value("${api.auth.password}")
    private String basicPass;

    // ======== TOKEN ========
    @PostMapping("/token/generation")
    public TokenResponse gerarToken(@RequestHeader("Authorization") String authorization) {

        if (!authorization.startsWith("Basic ")) throw new RuntimeException("Authorization inválida");

        String[] values = new String(Base64.getDecoder()
                .decode(authorization.replace("Basic ", "").trim())).split(":", 2);

        if (values[0].equals(basicUser) && values[1].equals(basicPass)) {
            String token = jwtUtil.generateToken(values[0], "ADMIN");
            return new TokenResponse(token);
        }

        throw new RuntimeException("Credenciais incorretas");
    }

    record TokenResponse(String token) {}

    // ======== CRUD USUÁRIO ========
    @PostMapping("/users/register")
    public User register(@RequestBody RegisterRequest req) {
        return userService.register(req);
    }

    @GetMapping("/users/list")
    public List<User> listarUsuarios() {
        return userService.listAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/users/me")
    public User getCurrentUser(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        return userService.getByUsername(username);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody RegisterRequest req) {
        return userService.updateUser(id, req);
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "Usuário removido com sucesso!";
    }

    @PostMapping("/users/{id}/change-password")
    public String changePassword(@PathVariable Long id, @RequestBody Map<String, String> body) {
        User user = userService.getUserById(id);

        String oldPass = body.get("oldPassword");
        String newPass = body.get("newPassword");

        if (!userService.matchesPassword(oldPass, user.getPassword()))
            throw new RuntimeException("Senha antiga incorreta");

        user.setPassword(userService.encodePassword(newPass));
        userService.updateUser(id, new RegisterRequest()); // salva a alteração
        return "Senha alterada com sucesso!";
    }
}
