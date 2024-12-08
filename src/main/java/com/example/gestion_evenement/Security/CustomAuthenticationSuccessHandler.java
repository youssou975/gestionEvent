package com.example.gestion_evenement.Security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Vérifier les rôles de l'utilisateur
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ORGANISATEUR")) {
                // Rediriger vers /dashboardadmin si rôle ORGANISATEUR
                response.sendRedirect("/evenements/dashboard");
                return;
            }
        }
        // Redirection par défaut pour les autres utilisateurs
        response.sendRedirect("/home");
    }
}


