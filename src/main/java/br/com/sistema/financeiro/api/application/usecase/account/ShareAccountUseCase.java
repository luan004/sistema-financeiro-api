package br.com.sistema.financeiro.api.application.usecase.account;

import br.com.sistema.financeiro.api.domain.exception.DomainException;
import br.com.sistema.financeiro.api.domain.model.Account;
import br.com.sistema.financeiro.api.domain.model.User;
import br.com.sistema.financeiro.api.domain.repository.AccountRepository;
import br.com.sistema.financeiro.api.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShareAccountUseCase {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public ShareAccountUseCase(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void execute(Long accountId, User owner, String email) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new DomainException("Conta não encontrada."));

        if (!account.isCreatedBy(owner)) {
            throw new DomainException("Somente o criador da conta pode compartilhar o acesso.");
        }

        User sharedUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new DomainException("Usuário não encontrado."));

        account.addUser(sharedUser);
    }
}