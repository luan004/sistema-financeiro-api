package br.com.sistema.financeiro.api.infrastructure.exception;

import br.com.sistema.financeiro.api.domain.exception.DomainException;

public class MissingRequestPayloadException extends DomainException {
    public MissingRequestPayloadException() {super("O payload esperado de requisição não foi atendido.");}
}
