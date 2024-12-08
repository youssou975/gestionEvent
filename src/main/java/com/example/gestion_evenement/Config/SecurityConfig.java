package com.example.gestion_evenement.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    // Injecte le CustomAuthenticationSuccessHandler
    public SecurityConfig(UserDetailsService userDetailsService, AuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Désactiver CSRF (non recommandé en production)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login", "/css/**","/paiements/**").permitAll() // Autorise ces pages pour les utilisateurs non authentifié
                        .requestMatchers("/dashboardadmin" , "/evenements/ajouter" , "/evenements/dashboard").hasAuthority("ORGANISATEUR")
                        .anyRequest().authenticated() // Toutes les autres pages nécessitent une authentification
                )
                .formLogin(form -> form
                        .loginPage("/login") // Page de connexion personnalisée
                        .successHandler(customAuthenticationSuccessHandler) // Utilise le handler personnalisé
                        .permitAll() // Permet l'accès à la page de connexion
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login") // Redirection après déconnexion
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}




