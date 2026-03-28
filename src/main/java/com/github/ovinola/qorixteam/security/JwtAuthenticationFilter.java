package com.github.ovinola.qorixteam.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Pega o cabeçalho Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final UUID userId;

        // Se não tiver o cabeçalho ou não começar com "Bearer ", passa direto (deixa o Spring bloquear depois)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extrai o token (tirando o "Bearer ")
        jwt = authHeader.substring(7);

        try {
            userId = jwtService.extractUserId(jwt);

            // 3. Se achou o usuário no token e a sessão do Spring ainda está vazia
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Valida se o token não está expirado ou adulterado
                if (jwtService.isTokenValid(jwt)) {
                    // 4. Cria o objeto de autenticação do Spring e diz: "Este cara está logado!"
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userId, null, Collections.emptyList() // Nenhuma role (admin/user) por enquanto
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Se o token for inválido, cai aqui e a requisição segue sem autenticação (sendo bloqueada)
            System.err.println("Erro ao validar token JWT: " + e.getMessage());
        }

        // 5. Continua o fluxo normal da requisição
        filterChain.doFilter(request, response);
    }
}