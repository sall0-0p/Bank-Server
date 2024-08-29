package com.bankserver.bankserver.account;

import com.bankserver.bankserver.user.User;
import com.bankserver.bankserver.user.UserRepository;
import com.bankserver.bankserver.utils.server.Server;
import com.bankserver.bankserver.utils.server.ServerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AccountREST {

    private final ServerRepository serverRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountService accountService;

    public AccountREST(ServerRepository serverRepository, AccountRepository accountRepository, UserRepository userRepository, AccountService accountService) {
        this.serverRepository = serverRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.accountService = accountService;
    }

    // GET
    // Gets data about account
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

    // POST
    // Creates new account
    // JSON Arguments (optional in nature):
    // displayName - String
    @PostMapping("/account/{id}")
    private ResponseEntity<?> createAccount(@RequestHeader("X-API-KEY") String apiKey, @PathVariable String id, @RequestBody Map<String, Object> data) {
        User user = userRepository.findById(UUID.fromString(id)).orElse(null);
        if (user == null) {
            return null;
        }

        Server server = serverRepository.findById(user.getWorldUUID()).orElse(null);
        if (server == null || !server.getApiKey().equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API Key");
        }

        if (accountRepository.findAccountsByOwner(user).size() >= user.getAccountLimit()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account limit exceeded");
        }

        Account newAccount = accountService.newAccount(user);

        if (data.containsKey("displayName")) {
            newAccount.setDisplayName(data.get("displayName").toString());
            newAccount = accountRepository.save(newAccount);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(newAccount);
    }

    // PATCH
    // Change property in account
    @PatchMapping("/account/{id}")
    private ResponseEntity<?> updateAccount(@RequestHeader("X-API-KEY") String apiKey, @PathVariable String id, @RequestBody Map<String, Object> data) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            return null;
        }

        User owner = account.getOwner();

        Server server = serverRepository.findById(owner.getWorldUUID()).orElse(null);
        if (server == null || !server.getApiKey().equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API Key");
        }

        for (String key : data.keySet()) {
            switch (key) {
                case "displayName":
                    account.setDisplayName(data.get(key).toString());
                    break;
                case "balance":
                    account.setBalance(Float.parseFloat(data.get(key).toString()));
                    break;
                case "suspended":
                    account.setSuspended(Boolean.parseBoolean(data.get(key).toString()));
                    break;
                case "creditLimit":
                    account.setCreditLimit(Float.parseFloat(data.get(key).toString()));
                    break;
                case "creditPercent":
                    account.setCreditPercent(Float.parseFloat(data.get(key).toString()));
                    break;
                default:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("There is no property '%s'", key));
            }
        }

        return ResponseEntity.status(200).body(accountRepository.save(account));
    }

    // DELETE
    // Soft delete account
    @DeleteMapping("/account/{id}")
    private ResponseEntity<?> deleteAccount(@RequestHeader("X-API-KEY") String apiKey, @PathVariable String id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            return null;
        }

        User owner = account.getOwner();

        Server server = serverRepository.findById(owner.getWorldUUID()).orElse(null);
        if (server == null || !server.getApiKey().equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API Key");
        }

        accountService.deleteAccount(account);

        return ResponseEntity.ok("Successfully deleted");
    }
}
