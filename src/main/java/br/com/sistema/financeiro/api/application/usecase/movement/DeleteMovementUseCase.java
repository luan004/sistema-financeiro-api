package br.com.sistema.financeiro.api.application.usecase.movement;

import br.com.sistema.financeiro.api.domain.exception.DomainException;
import br.com.sistema.financeiro.api.domain.model.Movement;
import br.com.sistema.financeiro.api.domain.model.User;
import br.com.sistema.financeiro.api.domain.repository.MovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteMovementUseCase {

    private final MovementRepository repository;

    public DeleteMovementUseCase(MovementRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void execute(Long id, User user) {
        Movement movement = repository.findById(id)
                .orElseThrow(() -> new DomainException("Movimentação não encontrada."));

        if (!movement.isOwnedBy(user)) {
            throw new DomainException("Você não tem acesso a esta movimentação.");
        }

        repository.delete(movement);
    }
}
