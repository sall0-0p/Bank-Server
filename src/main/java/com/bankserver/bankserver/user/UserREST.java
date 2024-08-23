package com.bankserver.bankserver.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserREST {
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserREST(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/user/{id}")
    private User getUserById(@PathVariable long id) {
        return userRepository.findById(id).orElse(null);
    }

    @PostMapping("/user/{username}")
    public User addUser(@PathVariable String username) {
        return userRepository.save(userService.createUser(username));
    }
}
