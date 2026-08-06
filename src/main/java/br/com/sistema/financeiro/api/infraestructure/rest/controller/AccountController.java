package br.com.sistema.financeiro.api.infraestructure.rest.controller;

import br.com.sistema.financeiro.api.domain.DomainException;
import br.com.sistema.financeiro.api.domain.account.Account;
import br.com.sistema.financeiro.api.domain.account.AccountRepository;
import br.com.sistema.financeiro.api.domain.user.User;
import br.com.sistema.financeiro.api.infraestructure.rest.security.AuthenticatedUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}, allowCredentials = "true")
@Transactional
public class AccountController {

    private final AccountRepository repo;

    public AccountController(
        AccountRepository repo
    ) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(
        @Valid @RequestBody AccountRequest request,
        @AuthenticatedUser User user
    ) {
        if (user == null) {
            throw new DomainException("Authorization header required");
        }

        Account account = new Account(request.description());
        account.addUser(user);
        Account saved = repo.save(account);

        return ResponseEntity.status(HttpStatus.CREATED).body(AccountResponse.from(saved));
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<AccountResponse>> list(@AuthenticatedUser User user) {
        if (user == null) {
            throw new DomainException("Authorization header required");
        }

        List<AccountResponse> accounts = repo.findByUsers_Id(user.getId()).stream().map(AccountResponse::from).toList();
        
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<AccountResponse> getById(@PathVariable Long id, @AuthenticatedUser User user) {
        if (user == null) {
            throw new DomainException("Authorization header required");
        }

        Account account = repo.findById(id).orElseThrow(() -> new DomainException("Account not found"));

        if (!account.getUsers().stream().anyMatch(existingUser -> existingUser.getId().equals(user.getId()))) {
            throw new DomainException("Account not accessible to the current user");
        }

        return ResponseEntity.ok(AccountResponse.from(account));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> update(@PathVariable Long id, @Valid @RequestBody AccountRequest request, @AuthenticatedUser User user) {
        if (user == null) {
            throw new DomainException("Authorization header required");
        }

        Account account = repo.findById(id).orElseThrow(() -> new DomainException("Account not found"));

        if (!account.getUsers().stream().anyMatch(existingUser -> existingUser.getId().equals(user.getId()))) {
            throw new DomainException("Account not accessible to the current user");
        }

        account.setDescription(request.description());
        return ResponseEntity.ok(AccountResponse.from(repo.save(account)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticatedUser User user) {
        if (user == null) {
            throw new DomainException("Authorization header required");
        }

        Account account = repo.findById(id).orElseThrow(() -> new DomainException("Account not found"));

        if (!account.getUsers().stream().anyMatch(existingUser -> existingUser.getId().equals(user.getId()))) {
            throw new DomainException("Account not accessible to the current user");
        }

        repo.delete(account);

        return ResponseEntity.noContent().build();
    }

    public record AccountRequest(
        String description
    ) {}

    public record AccountResponse(
        Long id,
        String description,
        String createdAt
    ) {
        static AccountResponse from(Account account) {
            return new AccountResponse(account.getId(), account.getDescription(), account.getCreatedAt().toString());
        }
    }
}
