package br.com.sistema.financeiro.api.application.usecase.account;

import br.com.sistema.financeiro.api.application.PaginationParams;
import br.com.sistema.financeiro.api.domain.model.Account;
import br.com.sistema.financeiro.api.domain.model.User;
import br.com.sistema.financeiro.api.domain.repository.AccountRepository;
import br.com.sistema.financeiro.api.domain.repository.MovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListAccountUseCase {

    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;

    public ListAccountUseCase(AccountRepository accountRepository, MovementRepository movementRepository) {
        this.accountRepository = accountRepository;
        this.movementRepository = movementRepository;
    }

    @Transactional(readOnly = true)
    public List<Account> execute(User user, Integer page, Integer limit) {
        PaginationParams pagination = PaginationParams.require(page, limit);

        List<Account> accounts = accountRepository.findByUsers_IdOrderByCreatedAtDesc(user.getId(), pagination.toPageable()).getContent();

        accounts.forEach(account ->
            account.setBalance(movementRepository.sumAmountByAccountId(account.getId()))
        );

        return accounts;
    }
}
