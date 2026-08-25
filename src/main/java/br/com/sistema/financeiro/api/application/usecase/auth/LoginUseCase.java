package br.com.sistema.financeiro.api.application.usecase.auth;

import br.com.sistema.financeiro.api.domain.exception.DomainException;
import br.com.sistema.financeiro.api.domain.model.Session;
import br.com.sistema.financeiro.api.domain.model.User;
import br.com.sistema.financeiro.api.domain.repository.SessionRepository;
import br.com.sistema.financeiro.api.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.HexFormat;

@Service
public class LoginUseCase {

    private static final int TOKEN_LENGTH = 64;

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public LoginUseCase(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    public String execute(String mail, String password) {
        User user = userRepository.findByEmail(mail)
                .filter(existingUser -> password.equals(existingUser.getPassword()))
                .orElseThrow(() -> new DomainException("Credenciais inválidas."));

        String token = generateToken();
        sessionRepository.save(new Session(user, token));

        return token;
    }

    private String generateToken() {
        byte[] randomBytes = new byte[TOKEN_LENGTH / 2];
        secureRandom.nextBytes(randomBytes);
        return HexFormat.of().formatHex(randomBytes);
    }
}
