package br.com.sistema.financeiro.api.application.usecase.auth;

import br.com.sistema.financeiro.api.domain.exception.DomainException;
import br.com.sistema.financeiro.api.domain.model.User;
import br.com.sistema.financeiro.api.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterUseCase {

    private final UserRepository userRepository;

    public RegisterUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void execute(String name, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new DomainException("Já existe um usuário com este e-mail.");
        }

        User user = new User(name, email, password);
        userRepository.save(user);
    }
}
