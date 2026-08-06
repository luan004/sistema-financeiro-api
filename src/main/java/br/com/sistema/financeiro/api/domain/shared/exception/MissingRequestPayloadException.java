package br.com.sistema.financeiro.api.domain.shared.exception;

import br.com.sistema.financeiro.api.domain.DomainException;

public class MissingRequestPayloadException extends DomainException {
    public MissingRequestPayloadException() {super("O payload esperado de requisição não foi atendido.");}
}
