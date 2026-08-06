package br.com.sistema.financeiro.api.infraestructure.rest.controller;

import br.com.sistema.financeiro.api.domain.DomainException;
import br.com.sistema.financeiro.api.domain.movement.Movement;
import br.com.sistema.financeiro.api.domain.movement.MovementRepository;
import br.com.sistema.financeiro.api.domain.user.User;
import br.com.sistema.financeiro.api.infraestructure.rest.PaginationParams;
import br.com.sistema.financeiro.api.infraestructure.rest.security.AuthenticatedUser;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/movements")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}, allowCredentials = "true")
@Transactional
public class MovementController {

    private final MovementRepository movementRepository;

    public MovementController(MovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }

    @PostMapping
    public ResponseEntity<MovementResponse> create(@Valid @RequestBody MovementRequest request, @AuthenticatedUser User user) {
        if (user == null) {
            throw new DomainException("Authorization header required");
        }

        if (request.amount().compareTo(BigDecimal.ZERO) == 0) {
            throw new DomainException("Amount cannot be zero");
        }

        Movement movement = new Movement(user, request.description(), request.amount());
        return ResponseEntity.status(HttpStatus.CREATED).body(MovementResponse.from(movementRepository.save(movement)));
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<MovementResponse>> list(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer limit,
        @AuthenticatedUser User user
    ) {
        if (user == null) {
            throw new DomainException("Authorization header required");
        }

        PaginationParams pagination = PaginationParams.require(page, limit);
        Page<Movement> movementsPage = movementRepository.findByCreator_Id(user.getId(), pagination.toPageable());

        List<MovementResponse> movements = movementsPage.getContent().stream()
                .map(MovementResponse::from)
                .toList();
        return ResponseEntity.ok(movements);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<MovementResponse> getById(@PathVariable Long id, @AuthenticatedUser User user) {
        if (user == null) {
            throw new DomainException("Authorization header required");
        }

        Movement movement = movementRepository.findById(id)
                .orElseThrow(() -> new DomainException("Movement not found"));

        if (!movement.getCreator().getId().equals(user.getId())) {
            throw new DomainException("Movement not accessible to the current user");
        }

        return ResponseEntity.ok(MovementResponse.from(movement));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticatedUser User user) {
        if (user == null) {
            throw new DomainException("Authorization header required");
        }

        Movement movement = movementRepository.findById(id)
                .orElseThrow(() -> new DomainException("Movement not found"));

        if (!movement.getCreator().getId().equals(user.getId())) {
            throw new DomainException("Movement not accessible to the current user");
        }

        movementRepository.delete(movement);
        return ResponseEntity.noContent().build();
    }

    public record MovementRequest(String description, BigDecimal amount) {
    }

    public record MovementResponse(Long id, String description, BigDecimal amount, String createdAt) {
        static MovementResponse from(Movement movement) {
            return new MovementResponse(
                movement.getId(),
                movement.getDescription(),
                movement.getAmount(),
                movement.getCreatedAt().toString()
            );
        }
    }
}
