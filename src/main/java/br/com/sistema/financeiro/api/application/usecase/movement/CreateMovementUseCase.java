package br.com.sistema.financeiro.api.application.usecase.movement;

import br.com.sistema.financeiro.api.domain.exception.DomainException;
import br.com.sistema.financeiro.api.domain.model.Account;
import br.com.sistema.financeiro.api.domain.model.Movement;
import br.com.sistema.financeiro.api.domain.model.User;
import br.com.sistema.financeiro.api.domain.repository.AccountRepository;
import br.com.sistema.financeiro.api.domain.repository.MovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CreateMovementUseCase {

    private final MovementRepository repository;
    private final AccountRepository accountRepository;

    public CreateMovementUseCase(MovementRepository repository, AccountRepository accountRepository) {
        this.repository = repository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Movement execute(User user, Long accountId, String description, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new DomainException("O valor não pode ser zero.");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new DomainException("Conta não encontrada."));

        if (!account.isOwnedBy(user)) {
            throw new DomainException("Você não tem acesso a esta conta.");
        }

        Movement movement = new Movement(user, account, description, amount);

        return repository.save(movement);
    }
}
