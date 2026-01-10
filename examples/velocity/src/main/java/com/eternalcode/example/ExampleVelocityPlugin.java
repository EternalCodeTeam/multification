package com.eternalcode.example;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

@Plugin(
        id = "example-velocity-plugin",
        name = "Example Velocity Plugin",
        version = "1.0.0",
        description = "An example Velocity plugin demonstrating Multification usage",
        authors = { "EternalCode" }
)
public class ExampleVelocityPlugin {

    private final ProxyServer server;

    @Inject
    public ExampleVelocityPlugin(ProxyServer server) {
        this.server = server;
    }

    void onProxyInitialize(ProxyInitializeEvent event) {

    }
}
