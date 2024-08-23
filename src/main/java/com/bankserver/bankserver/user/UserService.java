package com.bankserver.bankserver.user;

import com.bankserver.bankserver.account.Account;
import com.bankserver.bankserver.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserService {
    private final AccountService accountService;

    @Autowired
    public UserService(AccountService accountService) {
        this.accountService = accountService;
    }

    public User createUser(String username) {
        User user = new User(UUID.randomUUID(), username);
        user.setPersonalAccount(accountService.newAccount(user, user.getUsername()));

        return user;
    }
}
