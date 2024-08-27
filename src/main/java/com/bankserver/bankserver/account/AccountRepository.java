package com.bankserver.bankserver.account;

import com.bankserver.bankserver.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {
    public List<Account> findAccountsByOwner(User user);

    public List<Account> findAccountsByOwnerAndDeletedIsFalse(User user);
}
