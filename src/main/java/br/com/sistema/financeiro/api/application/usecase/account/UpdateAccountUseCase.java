package br.com.sistema.financeiro.api.application.usecase.account;

import br.com.sistema.financeiro.api.domain.exception.DomainException;
import br.com.sistema.financeiro.api.domain.model.Account;
import br.com.sistema.financeiro.api.domain.model.User;
import br.com.sistema.financeiro.api.domain.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateAccountUseCase {

    private final AccountRepository repository;

    public UpdateAccountUseCase(AccountRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void execute(Long id, User user, String description) {
        Account account = repository.findById(id)
                .orElseThrow(() -> new DomainException("Conta não encontrada."));

        if (!account.isOwnedBy(user)) {
            throw new DomainException("Você não tem acesso a esta conta.");
        }

        account.setDescription(description);
        repository.save(account);
    }
}
