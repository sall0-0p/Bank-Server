package com.bankserver.bankserver.user;

import com.bankserver.bankserver.account.Account;
import com.bankserver.bankserver.account.AccountService;
import com.bankserver.bankserver.utils.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserService {
    private final AccountService accountService;
    private final UserRepository userRepository;

    @Autowired
    public UserService(AccountService accountService, UserRepository userRepository) {
        this.accountService = accountService;
        this.userRepository = userRepository;
    }

    public User createUser(String username, Server server) {
        User user = new User(UUID.randomUUID(), username, server.getWorldUUID());
        user = userRepository.save(user);
        user.setPersonalAccountId(accountService.newAccount(user, user.getUsername()).getId());

        return user;
    }
}
