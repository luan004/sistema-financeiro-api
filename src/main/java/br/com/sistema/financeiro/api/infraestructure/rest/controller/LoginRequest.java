package br.com.sistema.financeiro.api.infraestructure.rest.controller;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "Mail is required")
    String mail,

    @NotBlank(message = "Password is required")
    String password
) {}
