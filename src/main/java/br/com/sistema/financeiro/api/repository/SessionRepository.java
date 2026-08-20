package br.com.sistema.financeiro.api.repository;

import br.com.sistema.financeiro.api.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByToken(String token);
}