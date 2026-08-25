package br.com.sistema.financeiro.api.infrastructure.controller;

import br.com.sistema.financeiro.api.application.usecase.auth.LoginUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final LoginUseCase loginUseCase;

    public AuthController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = loginUseCase.execute(request.mail(), request.password());

        return ResponseEntity.ok(new LoginResponse(token));
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
}
