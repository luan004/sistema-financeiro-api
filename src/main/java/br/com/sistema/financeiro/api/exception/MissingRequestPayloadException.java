package br.com.sistema.financeiro.api.exception;

public class MissingRequestPayloadException extends DomainException {
    public MissingRequestPayloadException() {super("O payload esperado de requisição não foi atendido.");}
}