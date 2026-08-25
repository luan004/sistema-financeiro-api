package br.com.sistema.financeiro.api.application.usecase.account;

import br.com.sistema.financeiro.api.domain.exception.DomainException;
import br.com.sistema.financeiro.api.domain.model.Account;
import br.com.sistema.financeiro.api.domain.model.User;
import br.com.sistema.financeiro.api.domain.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetAccountUseCase {

    private final AccountRepository repository;

    public GetAccountUseCase(AccountRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Account execute(Long id, User user) {
        Account account = repository.findById(id)
                .orElseThrow(() -> new DomainException("Conta não encontrada."));

        if (!account.isOwnedBy(user)) {
            throw new DomainException("Você não tem acesso a esta conta.");
        }

        return account;
    }
}
