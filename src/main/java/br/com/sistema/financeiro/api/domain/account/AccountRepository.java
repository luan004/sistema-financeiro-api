package br.com.sistema.financeiro.api.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByUsers_Id(Long userId);
}
