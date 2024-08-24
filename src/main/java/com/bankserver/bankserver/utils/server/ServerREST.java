package com.bankserver.bankserver.utils.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ServerREST {
    private final ServerService serverService;
    private final ServerRepository serverRepository;

    @Autowired
    public ServerREST(ServerService serverService, ServerRepository serverRepository) {
        this.serverService = serverService;
        this.serverRepository = serverRepository;
    }

    @PostMapping("/api/server/{worldId}")
    private Server registerServer(@PathVariable String worldId) {
        return serverRepository.save(serverService.registerServer(UUID.fromString(worldId)));
    }
}
