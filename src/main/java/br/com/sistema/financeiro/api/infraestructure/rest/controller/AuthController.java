package br.com.sistema.financeiro.api.infraestructure.rest.controller;

import br.com.sistema.financeiro.api.domain.DomainException;
import br.com.sistema.financeiro.api.domain.session.Session;
import br.com.sistema.financeiro.api.domain.session.SessionRepository;
import br.com.sistema.financeiro.api.domain.user.User;
import br.com.sistema.financeiro.api.domain.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}, allowCredentials = "true")
public class AuthController {

    private static final int TOKEN_LENGTH = 64;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public AuthController(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.mail());

        if (userOptional.isEmpty() || !request.password().equals(userOptional.get().getSenha())) {
            throw new DomainException("Invalid credentials");
        }

        User user = userOptional.get();
        String token = generateToken();
        Session session = new Session(user, token);
        sessionRepository.save(session);

        return ResponseEntity.ok(new LoginResponse(token));
    }

    private String generateToken() {
        byte[] randomBytes = new byte[TOKEN_LENGTH / 2];
        secureRandom.nextBytes(randomBytes);
        return HexFormat.of().formatHex(randomBytes);
    }
}
