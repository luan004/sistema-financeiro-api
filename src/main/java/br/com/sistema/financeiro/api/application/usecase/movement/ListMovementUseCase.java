package br.com.sistema.financeiro.api.application.usecase.movement;

import br.com.sistema.financeiro.api.application.PaginationParams;
import br.com.sistema.financeiro.api.domain.model.Movement;
import br.com.sistema.financeiro.api.domain.model.User;
import br.com.sistema.financeiro.api.domain.repository.MovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListMovementUseCase {

    private final MovementRepository repository;

    public ListMovementUseCase(MovementRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Movement> execute(User user, Integer page, Integer limit) {
        PaginationParams pagination = PaginationParams.require(page, limit);

        return repository.findByCreator_Id(user.getId(), pagination.toPageable()).getContent();
    }
}
