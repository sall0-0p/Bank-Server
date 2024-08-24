package com.bankserver.bankserver.user;

import com.bankserver.bankserver.utils.server.Server;
import com.bankserver.bankserver.utils.server.ServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserREST {
    private final UserRepository userRepository;
    private final UserService userService;
    private final ServerRepository serverRepository;

    @Autowired
    public UserREST(UserRepository userRepository, UserService userService, ServerRepository serverRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.serverRepository = serverRepository;
    }

    // Gets data about user from id
    @GetMapping("/user/{id}")
    private ResponseEntity<?> getUserById(@RequestHeader("X-API-KEY") String apiKey, @PathVariable String id) {
        User user = userRepository.findById(UUID.fromString(id)).orElse(null);
        if (user == null) {
            return null;
        }

        Server server = serverRepository.findById(user.getWorldUUID()).orElse(null);
        if (server == null || !server.getApiKey().toString().equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid API Key");
        }

        return ResponseEntity.ok(user);
    }

    // Creates user from username
    @PostMapping("/user/{username}")
    public ResponseEntity<?> addUser(@RequestHeader("X-API-KEY") String apiKey, @PathVariable String username) {

        Server server = serverRepository.findByApiKey(UUID.fromString(apiKey));
        if (server == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid API Key");
        }

        return ResponseEntity.ok(userService.createUser(username, server));
    }
}
