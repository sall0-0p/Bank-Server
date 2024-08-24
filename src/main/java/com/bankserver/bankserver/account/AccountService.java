package com.bankserver.bankserver.account;

import com.bankserver.bankserver.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account newAccount(User owner) {
        return new Account(owner);
    }

    public Account newAccount (User owner, String displayName) {
        Account account = new Account(owner, displayName);

        return accountRepository.save(account);
    }
}
