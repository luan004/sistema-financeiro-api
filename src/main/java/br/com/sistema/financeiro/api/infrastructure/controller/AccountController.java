package br.com.sistema.financeiro.api.infrastructure.controller;

import br.com.sistema.financeiro.api.application.usecase.account.CreateAccountUseCase;
import br.com.sistema.financeiro.api.application.usecase.account.DeleteAccountUseCase;
import br.com.sistema.financeiro.api.application.usecase.account.GetAccountUseCase;
import br.com.sistema.financeiro.api.application.usecase.account.ListAccountUseCase;
import br.com.sistema.financeiro.api.application.usecase.account.UpdateAccountUseCase;
import br.com.sistema.financeiro.api.domain.model.Account;
import br.com.sistema.financeiro.api.domain.model.User;
import br.com.sistema.financeiro.api.infrastructure.security.AuthenticatedUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final CreateAccountUseCase createAccount;
    private final GetAccountUseCase getAccount;
    private final ListAccountUseCase listAccount;
    private final UpdateAccountUseCase updateAccount;
    private final DeleteAccountUseCase deleteAccount;

    public AccountController(
        CreateAccountUseCase createAccount,
        GetAccountUseCase getAccount,
        ListAccountUseCase listAccount,
        UpdateAccountUseCase updateAccount,
        DeleteAccountUseCase deleteAccount
    ) {
        this.createAccount = createAccount;
        this.getAccount = getAccount;
        this.listAccount = listAccount;
        this.updateAccount = updateAccount;
        this.deleteAccount = deleteAccount;
    }

    @PostMapping
    public ResponseEntity<Account> create(@Valid @RequestBody AccountRequest request, @AuthenticatedUser User user) {
        Account account = createAccount.execute(user, request.description());

        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping
    public ResponseEntity<List<Account>> list(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer limit,
        @AuthenticatedUser User user
    ) {
        return ResponseEntity.ok(listAccount.execute(user, page, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getById(@PathVariable Long id, @AuthenticatedUser User user) {
        return ResponseEntity.ok(getAccount.execute(id, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody AccountRequest request, @AuthenticatedUser User user) {
        updateAccount.execute(id, user, request.description());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticatedUser User user) {
        deleteAccount.execute(id, user);

        return ResponseEntity.noContent().build();
    }

    public record AccountRequest(String description) {}
}
