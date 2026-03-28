package com.github.ovinola.qorixteam.controller;

import com.github.ovinola.qorixteam.dto.AuthResponse;
import com.github.ovinola.qorixteam.dto.LoginRequest;
import com.github.ovinola.qorixteam.model.AppUser;
import com.github.ovinola.qorixteam.repository.AppUserRepository;
import com.github.ovinola.qorixteam.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // 1. Busca usuário
        AppUser user = userRepository.findByUsername(request.username())
                .orElse(null);

        // 2. Valida se existe e se a senha bate
        if (user == null || !passwordEncoder.matches(request.password(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos");
        }

        // 3. Gera o Token JWT
        String token = jwtService.generateToken(user.getId(), user.getUsername());

        // 4. Retorna a resposta que o Next.js vai guardar
        return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getUsername()));
    }
}