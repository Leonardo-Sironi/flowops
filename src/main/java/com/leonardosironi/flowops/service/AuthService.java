
package com.leonardosironi.flowops.service;

import com.leonardosironi.flowops.domain.Employee;
import com.leonardosironi.flowops.dto.LoginRequest;
import com.leonardosironi.flowops.dto.LoginResponse;
import com.leonardosironi.flowops.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;

@Service
public class AuthService {

    private static final Duration TOKEN_TTL = Duration.ofMinutes(60);

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public AuthService(
            EmployeeRepository employeeRepository,
            PasswordEncoder passwordEncoder,
            JwtEncoder jwtEncoder
    ) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public LoginResponse login(LoginRequest request) {
        Employee employee = employeeRepository
                .findByEmailIgnoreCase(request.getEmail().trim())
                .filter(e -> e.getPasswordHash() != null
                        && passwordEncoder.matches(
                        request.getPassword(),
                        e.getPasswordHash()
                ))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password"
                ));

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("flowops")
                .issuedAt(now)
                .expiresAt(now.plus(TOKEN_TTL))
                .subject(employee.getEmail())
                .claim("role", employee.getRole().name())
                .build();

        JwsHeader header = JwsHeader
                .with(MacAlgorithm.HS256)
                .build();

        String token = jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
        ).getTokenValue();

        return new LoginResponse(token);
    }
}
