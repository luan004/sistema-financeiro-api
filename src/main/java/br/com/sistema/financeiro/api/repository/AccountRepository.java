package br.com.sistema.financeiro.api.repository;

import br.com.sistema.financeiro.api.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Page<Account> findByUsers_IdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}