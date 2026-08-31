package br.com.sistema.financeiro.api.infrastructure.controller;

import br.com.sistema.financeiro.api.application.usecase.auth.LoginUseCase;
import br.com.sistema.financeiro.api.application.usecase.auth.RegisterUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;

    public AuthController(LoginUseCase loginUseCase, RegisterUseCase registerUseCase) {
        this.loginUseCase = loginUseCase;
        this.registerUseCase = registerUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = loginUseCase.execute(request.mail(), request.password());

        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        registerUseCase.execute(request.name(), request.email(), request.password());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public record LoginRequest(
        @NotBlank(message = "O email é obrigatório.")
        String mail,

        @NotBlank(message = "A senha é obrigatória.")
        String password
    ) {}

    public record LoginResponse(
        String token
    ) {}

    public record RegisterRequest(
        @NotBlank(message = "O nome é obrigatório.")
        String name,

        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "O email deve ser válido.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        String password
    ) {}
}
