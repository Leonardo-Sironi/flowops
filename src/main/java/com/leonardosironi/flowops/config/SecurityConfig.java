
package com.leonardosironi.flowops.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        JwtGrantedAuthoritiesConverter authoritiesConverter =
                new JwtGrantedAuthoritiesConverter();

        authoritiesConverter.setAuthoritiesClaimName("role");
        authoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter =
                new JwtAuthenticationConverter();

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
                authoritiesConverter
        );

        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(auth -> auth

                        // Cadastro e login públicos
                        .requestMatchers(HttpMethod.POST, "/employees")
                        .permitAll()
                        .requestMatchers("/auth/**")
                        .permitAll()

                        // Consultar tarefas
                        .requestMatchers(
                                HttpMethod.GET,
                                "/tasks",
                                "/tasks/**"
                        ).hasAnyRole("EMPLOYEE", "MANAGER")

                        // Criar tarefas: somente gerente
                        .requestMatchers(HttpMethod.POST, "/tasks")
                        .hasRole("MANAGER")

                        // Operações administrativas: somente gerente
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/tasks/*/assign/*",
                                "/tasks/*/approve",
                                "/tasks/*/reject",
                                "/tasks/*/cancel"
                        ).hasRole("MANAGER")

                        // Executar tarefas
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/tasks/*/start",
                                "/tasks/*/complete"
                        ).hasAnyRole("EMPLOYEE", "MANAGER")

                        // Demais rotas exigem autenticação
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(
                                        jwtAuthenticationConverter
                                )
                        )
                )
                .build();
    }
}
