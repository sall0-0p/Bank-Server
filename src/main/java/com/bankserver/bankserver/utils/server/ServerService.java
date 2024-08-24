package com.bankserver.bankserver.utils.server;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ServerService {
    public Server registerServer(UUID worldUUID) {
        return new Server(worldUUID);
    }
}
