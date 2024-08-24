package com.bankserver.bankserver.account;

import com.bankserver.bankserver.user.User;
import com.bankserver.bankserver.utils.server.Server;
import com.bankserver.bankserver.utils.server.ServerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AccountREST {

    private final ServerRepository serverRepository;
    private final AccountRepository accountRepository;

    public AccountREST(ServerRepository serverRepository, AccountRepository accountRepository) {
        this.serverRepository = serverRepository;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/account/{id}")
    private ResponseEntity<?> getAccount(@RequestHeader("X-API-KEY") String apiKey, @PathVariable String id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            return null;
        }

        User owner = account.getOwner();

        Server server = serverRepository.findById(owner.getWorldUUID()).orElse(null);
        if (server == null || !server.getApiKey().toString().equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid API Key");
        }

        return ResponseEntity.ok(account);
    }
}
