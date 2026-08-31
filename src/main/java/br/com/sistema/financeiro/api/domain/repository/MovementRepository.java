package br.com.sistema.financeiro.api.domain.repository;

import br.com.sistema.financeiro.api.domain.model.Movement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface MovementRepository extends JpaRepository<Movement, Long> {

    @EntityGraph(attributePaths = "account")
    Page<Movement> findByCreator_Id(Long creatorId, Pageable pageable);

    boolean existsByAccount_Id(Long accountId);

    @Query("SELECT COALESCE(SUM(m.amount), 0) FROM Movement m WHERE m.account.id = :accountId")
    BigDecimal sumAmountByAccountId(@Param("accountId") Long accountId);
}
