package br.com.sistema.financeiro.api.infraestructure.rest;

import br.com.sistema.financeiro.api.domain.DomainException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record PaginationParams(int page, int limit) {

    public static PaginationParams require(Integer page, Integer limit) {
        if (page == null) {
            throw new DomainException("Parameter 'page' is required");
        }

        if (limit == null) {
            throw new DomainException("Parameter 'limit' is required");
        }

        if (page < 1) {
            throw new DomainException("Parameter 'page' must be >= 1");
        }

        if (limit < 1 || limit > 100) {
            throw new DomainException("Parameter 'limit' must be between 1 and 100");
        }

        return new PaginationParams(page, limit);
    }

    public Pageable toPageable() {
        return PageRequest.of(page - 1, limit);
    }
}
