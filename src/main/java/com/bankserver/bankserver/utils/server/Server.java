package com.bankserver.bankserver.utils.server;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.UUID;

@Entity
@Table(name = "servers")
public class Server {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID worldUUID;

    @Column(nullable = false, updatable = false)
    private UUID apiKey;

    public Server() {}

    public Server(UUID worldUUID) {
        this.worldUUID = worldUUID;
        this.apiKey = UUID.randomUUID();
    }

    public UUID getWorldUUID() {
        return worldUUID;
    }

    public UUID getApiKey() {
        return apiKey;
    }
}
