package br.com.sistema.financeiro.api.controller;

import br.com.sistema.financeiro.api.exception.DomainException;
import br.com.sistema.financeiro.api.model.Account;
import br.com.sistema.financeiro.api.model.User;
import br.com.sistema.financeiro.api.repository.AccountRepository;
import br.com.sistema.financeiro.api.security.AuthenticatedUser;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
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
        Account account = new Account(request.description());
        account.addUser(user);
        Account saved = repo.save(account);

        return ResponseEntity.status(HttpStatus.CREATED).body(AccountResponse.from(saved));
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<AccountResponse>> list(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer limit,
        @AuthenticatedUser User user
    ) {
        PaginationParams pagination = PaginationParams.require(page, limit);
        Page<Account> accountsPage = repo.findByUsers_IdOrderByCreatedAtDesc(user.getId(), pagination.toPageable());

        List<AccountResponse> accounts = accountsPage.getContent().stream().map(AccountResponse::from).toList();

        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<AccountResponse> getById(@PathVariable Long id, @AuthenticatedUser User user) {
        Account account = repo.findById(id).orElseThrow(() -> new DomainException("Conta não encontrada."));

        if (!account.getUsers().stream().anyMatch(existingUser -> existingUser.getId().equals(user.getId()))) {
            throw new DomainException("Você não tem acesso a esta conta.");
        }

        return ResponseEntity.ok(AccountResponse.from(account));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> update(@PathVariable Long id, @Valid @RequestBody AccountRequest request, @AuthenticatedUser User user) {
        Account account = repo.findById(id).orElseThrow(() -> new DomainException("Conta não encontrada."));

        if (!account.getUsers().stream().anyMatch(existingUser -> existingUser.getId().equals(user.getId()))) {
            throw new DomainException("Você não tem acesso a esta conta.");
        }

        account.setDescription(request.description());
        return ResponseEntity.ok(AccountResponse.from(repo.save(account)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticatedUser User user) {
        Account account = repo.findById(id).orElseThrow(() -> new DomainException("Conta não encontrada."));

        if (!account.getUsers().stream().anyMatch(existingUser -> existingUser.getId().equals(user.getId()))) {
            throw new DomainException("Você não tem acesso a esta conta.");
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