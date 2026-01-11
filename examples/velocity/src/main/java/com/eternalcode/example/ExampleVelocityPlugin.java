package com.eternalcode.example;

import com.eternalcode.example.command.ReloadCommand;
import com.eternalcode.example.command.SwitchCommand;
import com.eternalcode.example.config.ConfigurationManager;
import com.eternalcode.example.config.MessagesConfig;
import com.eternalcode.example.notice.ExampleMultification;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.velocity.LiteVelocityFactory;

import java.nio.file.Path;

@Plugin(
        id = "example-velocity-plugin",
        name = "Example Velocity Plugin",
        version = "1.0.0",
        description = "An example Velocity plugin demonstrating Multification usage",
        authors = { "EternalCode" }
)
public class ExampleVelocityPlugin {

    private final ProxyServer server;
    private final Path dataDirectory;

    private LiteCommands<CommandSource> liteCommands;

    @Inject
    public ExampleVelocityPlugin(ProxyServer server, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    void onProxyInitialize(ProxyInitializeEvent event) {
        MessagesConfig messagesConfig = new MessagesConfig();
        ExampleMultification multification = new ExampleMultification((Plugin) this, this.server, messagesConfig);

        ConfigurationManager configurationManager = new ConfigurationManager(this.dataDirectory.toFile(),
                multification.getNoticeRegistry());
        configurationManager.load(messagesConfig, "messages.yml");

        this.liteCommands = LiteVelocityFactory.builder(this.server)
                .commands(
                        new ReloadCommand(configurationManager, multification),
                        new SwitchCommand(multification))
                .build();

        this.server.getEventManager().register(this, new PlayerConnectListener(multification));
    }

    @Subscribe
    void onProxyShutdown(ProxyShutdownEvent event) {
        if (this.liteCommands != null) {
            this.liteCommands.unregister();
        }
    }
}
