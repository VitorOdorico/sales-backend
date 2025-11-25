package com.qualix.backend.sales.controllers.Auth;

import com.qualix.backend.sales.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${api.auth.user}")
    private String basicUser;

    @Value("${api.auth.password}")
    private String basicPass;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/token/generation")
    public TokenResponse gerarToken(@RequestHeader("Authorization") String authorization) {

        System.out.println("➡️ ENTROU NO CONTROLLER");
        System.out.println("Authorization recebido: " + authorization);

        if (!authorization.startsWith("Basic ")) {
            System.out.println("❌ Authorization inválida");
            throw new RuntimeException("Authorization inválida");
        }

        String base64Credentials = authorization.replace("Basic ", "").trim();
        System.out.println("Base64 recebido: " + base64Credentials);

        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        System.out.println("Credenciais decodificadas: " + credentials);

        String[] values = credentials.split(":", 2);
        String user = values[0];
        String pass = values[1];

        System.out.println("USER: " + user);
        System.out.println("PASS: " + pass);

        if (user.equals(basicUser) && pass.equals(basicPass)) {

            // 🔥 GERA JWT AQUI
            String token = jwtUtil.generateToken(user);

            System.out.println("TOKEN JWT GERADO: " + token);

            return new TokenResponse(token);
        }

        System.out.println("❌ Credenciais incorretas");
        throw new RuntimeException("Credenciais incorretas");
    }


    record TokenResponse(String token) {}
}
