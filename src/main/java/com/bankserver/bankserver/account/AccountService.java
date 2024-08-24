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
        return new Account(generateAccountId(), owner);
    }

    public Account newAccount (User owner, String displayName) {
        Account account = new Account(generateAccountId(), owner, displayName);

        return accountRepository.save(account);
    }

    // misc methods

    private String generateAccountId() {
        String result = generateEightDigits();

        if (accountRepository.existsById(result)) {
            return generateAccountId();
        } else {
            return result;
        }
    }

    private String generateEightDigits() {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            result.append(Math.round(Math.random() * 9));
        }

        return result.toString();
    }
}
