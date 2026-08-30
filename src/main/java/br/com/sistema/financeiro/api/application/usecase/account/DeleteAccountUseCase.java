package br.com.sistema.financeiro.api.application.usecase.account;

import br.com.sistema.financeiro.api.domain.exception.DomainException;
import br.com.sistema.financeiro.api.domain.model.Account;
import br.com.sistema.financeiro.api.domain.model.User;
import br.com.sistema.financeiro.api.domain.repository.AccountRepository;
import br.com.sistema.financeiro.api.domain.repository.MovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteAccountUseCase {

    private final AccountRepository repository;
    private final MovementRepository movementRepository;

    public DeleteAccountUseCase(AccountRepository repository, MovementRepository movementRepository) {
        this.repository = repository;
        this.movementRepository = movementRepository;
    }

    @Transactional
    public void execute(Long id, User user) {
        Account account = repository.findById(id)
                .orElseThrow(() -> new DomainException("Conta não encontrada."));

        if (!account.isOwnedBy(user)) {
            throw new DomainException("Você não tem acesso a esta conta.");
        }

        if (movementRepository.existsByAccount_Id(account.getId())) {
            throw new DomainException("Não é possível excluir uma conta que possui movimentações associadas.");
        }

        repository.delete(account);
    }
}
