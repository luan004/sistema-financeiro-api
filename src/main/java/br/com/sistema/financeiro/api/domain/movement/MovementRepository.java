package br.com.sistema.financeiro.api.domain.movement;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovementRepository extends JpaRepository<Movement, Long> {

    List<Movement> findByCreator_Id(Long creatorId);
}
