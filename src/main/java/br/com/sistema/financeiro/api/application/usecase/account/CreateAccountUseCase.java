package br.com.sistema.financeiro.api.application.usecase.account;

import br.com.sistema.financeiro.api.domain.model.Account;
import br.com.sistema.financeiro.api.domain.model.User;
import br.com.sistema.financeiro.api.domain.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateAccountUseCase {

    private final AccountRepository repository;

    public CreateAccountUseCase(AccountRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Account execute(User user, String description) {
        Account account = new Account(description);
        account.addUser(user);

        return repository.save(account);
    }
}
