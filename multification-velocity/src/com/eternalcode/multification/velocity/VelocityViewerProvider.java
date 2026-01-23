package com.eternalcode.multification.velocity;

import com.eternalcode.multification.viewer.ViewerProvider;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class VelocityViewerProvider implements ViewerProvider<CommandSource> {

    private final ProxyServer server;

    public VelocityViewerProvider(ProxyServer server) {
        this.server = server;
    }

    @Override
    public CommandSource console() {
        return this.server.getConsoleCommandSource();
    }

    @Override
    public CommandSource player(UUID uuid) {
        return this.server.getplayer(uuid).orElse(null);
    }

    @Override
    public Collection<CommandSource> onlinePlayers() {
        return new ArrayList<>(server.getAllPlayers());
    }

    @Override
    public Collection<CommandSource> onlinePlayers(String permission) {
        return server.getAllPlayers()
                .stream()
                .filter(player -> player.hasPermission(permission))
                .map(player -> (CommandSource) player)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<CommandSource> all() {
        Collection<CommandSource> viewers = this.onlinePlayers();
        viewers.add(this.console());
        return viewers;
    }
}
