package com.eternalcode.multification.paper;

import com.eternalcode.multification.viewer.ViewerProvider;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class PaperViewerProvider implements ViewerProvider<CommandSender> {

    @Override
    public CommandSender console() {
        return Bukkit.getConsoleSender();
    }

    @Override
    public CommandSender player(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);

        if (player == null || !player.isOnline()) {
            return null;
        }

        return player;
    }

    @Override
    public Collection<CommandSender> all() {
        return Bukkit.getOnlinePlayers()
                .stream()
                .map(player -> (CommandSender) player)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<CommandSender> onlinePlayers() {
        return Bukkit.getOnlinePlayers()
                .stream()
                .map(player -> (CommandSender) player)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<CommandSender> onlinePlayers(String permission) {
        if (permission == null || permission.isEmpty()) {
            return onlinePlayers();
        }

        return Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> player.hasPermission(permission))
                .map(player -> (CommandSender) player)
                .collect(Collectors.toList());
    }
}
