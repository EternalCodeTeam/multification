package com.eternalcode.example.bukkit;

import com.eternalcode.multification.viewer.ViewerProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

class BukkitViewerProvider implements ViewerProvider<CommandSender> {
    @Override
    public CommandSender console() {
        return Bukkit.getConsoleSender();
    }

    @Override
    public CommandSender player(UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }

    @Override
    public Collection<CommandSender> onlinePlayers() {
        return Collections.unmodifiableCollection(Bukkit.getOnlinePlayers());
    }

    @Override
    public Collection<CommandSender> all() {
        Collection<CommandSender> viewers = new ArrayList<>(this.onlinePlayers());
        viewers.add(this.console());

        return Collections.unmodifiableCollection(viewers);
    }
}
