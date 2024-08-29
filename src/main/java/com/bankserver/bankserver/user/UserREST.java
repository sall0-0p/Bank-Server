package com.bankserver.bankserver.user;

import com.bankserver.bankserver.account.AccountRepository;
import com.bankserver.bankserver.utils.server.Server;
import com.bankserver.bankserver.utils.server.ServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
public class UserREST {
    private final Pattern UUID_REGEX = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    private final UserRepository userRepository;
    private final UserService userService;
    private final ServerRepository serverRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public UserREST(UserRepository userRepository, UserService userService, ServerRepository serverRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.serverRepository = serverRepository;
        this.accountRepository = accountRepository;
    }

    // GET
    // Gets data about user from id or username
    @GetMapping("/user/{id}")
    private ResponseEntity<?> getUserById(@RequestHeader("X-API-KEY") String apiKey, @PathVariable String id) {
        boolean isUUID = UUID_REGEX.matcher(id).matches();
        User user;

        if (isUUID) {
            user = userRepository.findById(UUID.fromString(id)).orElse(null);
        } else {
            user = userRepository.findByUsername(id).orElse(null);
        }

        if (user == null) {
            return null;
        }

        Server server = serverRepository.findById(user.getWorldUUID()).orElse(null);
        if (server == null || !server.getApiKey().equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API Key");
        }

        return ResponseEntity.ok(user);
    }

    // GET
    // Gets list of accounts user owns :D
    @GetMapping("/user/{id}/accounts")
    private ResponseEntity<?> getUserAccounts(@RequestHeader("X-API-KEY") String apiKey, @PathVariable String id) {
        User user = userRepository.findById(UUID.fromString(id)).orElse(null);
        if (user == null) {
            return null;
        }

        Server server = serverRepository.findById(user.getWorldUUID()).orElse(null);
        if (server == null || !server.getApiKey().equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API Key");
        }

        return ResponseEntity.ok(accountRepository.findAccountsByOwnerAndDeletedIsFalse(user));
    }

    // POST
    // Creates user from username
    // TODO: Refactor to UUID & username instead of plain username!
    @PostMapping("/user/{username}")
    public ResponseEntity<?> addUser(@RequestHeader("X-API-KEY") String apiKey, @PathVariable String username) {
        Server server = serverRepository.findByApiKey(apiKey);
        if (server == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API Key");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(username, server));
    }

    // PATCH
    // Updates property of user
    @PatchMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@RequestHeader("X-API-KEY") String apiKey, @PathVariable String id, @RequestBody Map<String, Object> updates) {
        User user = userRepository.findById(UUID.fromString(id)).orElse(null);
        if (user == null) {
            return null;
        }

        Server server = serverRepository.findById(user.getWorldUUID()).orElse(null);
        if (server == null || !server.getApiKey().equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API Key");
        }

        if (user.isDeleted()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is deleted");
        }

        for (String key : updates.keySet()) {
            if (!key.equals("suspended") && user.isSuspended()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is suspended");
            }

            switch (key) {
                case "suspended":
                    user.setSuspended(Boolean.parseBoolean(updates.get(key).toString()));
                    break;
                case "accountLimit":
                    user.setAccountLimit(Integer.parseInt(updates.get(key).toString()));
                    break;
                default:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("There is no property '%s'", key));
            }
        }

        userRepository.save(user);

        return ResponseEntity.status(200).body(user);
    }

    // DELETE
    // Soft deletes user
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@RequestHeader("X-API-KEY") String apiKey, @PathVariable String id) {
        User user = userRepository.findById(UUID.fromString(id)).orElse(null);
        if (user == null) {
            return null;
        }

        Server server = serverRepository.findById(user.getWorldUUID()).orElse(null);
        if (server == null || !server.getApiKey().equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid API Key");
        }

        userService.deleteUser(user);

        return ResponseEntity.ok("Successfully deleted");
    }
}
