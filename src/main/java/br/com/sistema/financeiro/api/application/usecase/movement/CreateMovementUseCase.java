package br.com.sistema.financeiro.api.application.usecase.movement;

import br.com.sistema.financeiro.api.domain.exception.DomainException;
import br.com.sistema.financeiro.api.domain.model.Movement;
import br.com.sistema.financeiro.api.domain.model.User;
import br.com.sistema.financeiro.api.domain.repository.MovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CreateMovementUseCase {

    private final MovementRepository repository;

    public CreateMovementUseCase(MovementRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Movement execute(User user, String description, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new DomainException("O valor não pode ser zero.");
        }

        Movement movement = new Movement(user, description, amount);

        return repository.save(movement);
    }
}
