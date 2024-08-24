package com.bankserver.bankserver.utils.server;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServerRepository extends JpaRepository<Server, UUID> {
    public Server findByApiKey(String apiKey);
}
