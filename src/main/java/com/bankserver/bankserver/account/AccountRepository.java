package com.bankserver.bankserver.account;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface AccountRepository extends Repository<Account, Long> {
    Account save(Account account);

    Optional<Account> findById(Long id);
}
