package br.com.sistema.financeiro.api.exception;

/**
 * Exceção base para violações de regras do negócio.
 * A camada HTTP fará a tradução para a resposta apropriada quando necessário.
 */
public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }
}