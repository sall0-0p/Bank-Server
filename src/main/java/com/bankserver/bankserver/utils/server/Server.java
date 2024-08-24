package com.bankserver.bankserver.utils.server;

import com.bankserver.bankserver.utils.HASHGenerator;
import jakarta.persistence.*;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Entity
@Table(name = "servers")
public class Server {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID worldUUID;

    @Column(nullable = false, updatable = false)
    private String apiKey;

    public Server() {}

    public Server(UUID worldUUID) {
        this.worldUUID = worldUUID;
        try {
            this.apiKey = HASHGenerator.createSHAHash(UUID.randomUUID().toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public UUID getWorldUUID() {
        return worldUUID;
    }

    public String getApiKey() {
        return apiKey;
    }
}
