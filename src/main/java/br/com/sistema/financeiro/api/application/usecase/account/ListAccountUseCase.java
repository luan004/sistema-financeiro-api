package br.com.sistema.financeiro.api.application.usecase.account;

import br.com.sistema.financeiro.api.application.PaginationParams;
import br.com.sistema.financeiro.api.domain.model.Account;
import br.com.sistema.financeiro.api.domain.model.User;
import br.com.sistema.financeiro.api.domain.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListAccountUseCase {

    private final AccountRepository repository;

    public ListAccountUseCase(AccountRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Account> execute(User user, Integer page, Integer limit) {
        PaginationParams pagination = PaginationParams.require(page, limit);

        return repository.findByUsers_IdOrderByCreatedAtDesc(user.getId(), pagination.toPageable()).getContent();
    }
}
