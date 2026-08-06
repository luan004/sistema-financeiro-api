package br.com.sistema.financeiro.api.infraestructure.http.handler;

import br.com.sistema.financeiro.api.domain.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(DomainException.class)
    ProblemDetail handleDomainException(DomainException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage());
        problem.setTitle("Regra de domínio violada");
        return problem;
    }
}
