package br.com.sistema.financeiro.api.application;

import br.com.sistema.financeiro.api.domain.exception.DomainException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record PaginationParams(int page, int limit) {

    public static PaginationParams require(Integer page, Integer limit) {
        if (page == null) {
            throw new DomainException("O parâmetro 'page' é obrigatório.");
        }

        if (limit == null) {
            throw new DomainException("O parâmetro 'limit' é obrigatório.");
        }

        if (page < 1) {
            throw new DomainException("O parâmetro 'page' deve ser maior ou igual a 1.");
        }

        if (limit < 1 || limit > 100) {
            throw new DomainException("O parâmetro 'limit' deve estar entre 1 e 100.");
        }

        return new PaginationParams(page, limit);
    }

    public Pageable toPageable() {
        return PageRequest.of(page - 1, limit);
    }
}
