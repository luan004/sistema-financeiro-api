package br.com.sistema.financeiro.api.domain;

/**
 * Exceção base para violações de regras do domínio.
 * A camada HTTP fará a tradução para a resposta apropriada quando necessário.
 */
public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }
}
