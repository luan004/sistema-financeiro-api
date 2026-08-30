package br.com.sistema.financeiro.api.infrastructure.controller;

import br.com.sistema.financeiro.api.application.usecase.movement.CreateMovementUseCase;
import br.com.sistema.financeiro.api.application.usecase.movement.DeleteMovementUseCase;
import br.com.sistema.financeiro.api.application.usecase.movement.GetMovementUseCase;
import br.com.sistema.financeiro.api.application.usecase.movement.ListMovementUseCase;
import br.com.sistema.financeiro.api.domain.model.Movement;
import br.com.sistema.financeiro.api.domain.model.User;
import br.com.sistema.financeiro.api.infrastructure.security.AuthenticatedUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/movements")
public class MovementController {

    private final CreateMovementUseCase createMovement;
    private final GetMovementUseCase getMovement;
    private final ListMovementUseCase listMovement;
    private final DeleteMovementUseCase deleteMovement;

    public MovementController(
        CreateMovementUseCase createMovement,
        GetMovementUseCase getMovement,
        ListMovementUseCase listMovement,
        DeleteMovementUseCase deleteMovement
    ) {
        this.createMovement = createMovement;
        this.getMovement = getMovement;
        this.listMovement = listMovement;
        this.deleteMovement = deleteMovement;
    }

    @PostMapping
    public ResponseEntity<Movement> create(@Valid @RequestBody MovementRequest request, @AuthenticatedUser User user) {
        Movement movement = createMovement.execute(user, request.accountId(), request.description(), request.amount());

        return ResponseEntity.status(HttpStatus.CREATED).body(movement);
    }

    @GetMapping
    public ResponseEntity<List<Movement>> list(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer limit,
        @AuthenticatedUser User user
    ) {
        return ResponseEntity.ok(listMovement.execute(user, page, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movement> getById(@PathVariable Long id, @AuthenticatedUser User user) {
        return ResponseEntity.ok(getMovement.execute(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticatedUser User user) {
        deleteMovement.execute(id, user);

        return ResponseEntity.noContent().build();
    }

    public record MovementRequest(@NotNull(message = "A conta é obrigatória.") Long accountId, String description, BigDecimal amount) {}
}
