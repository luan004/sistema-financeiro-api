package br.com.sistema.financeiro.api.domain.repository;

import br.com.sistema.financeiro.api.domain.model.Movement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovementRepository extends JpaRepository<Movement, Long> {

    Page<Movement> findByCreator_Id(Long creatorId, Pageable pageable);
}
