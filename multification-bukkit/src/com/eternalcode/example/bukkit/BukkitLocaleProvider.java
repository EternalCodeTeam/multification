package com.eternalcode.example.bukkit;

import com.eternalcode.multification.locate.LocaleProvider;
import java.util.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitLocaleProvider implements LocaleProvider<CommandSender> {

    @Override
    public @NotNull Locale provide(CommandSender commandSender) {
        return commandSender instanceof Player player ? new Locale(player.getLocale()) : Locale.getDefault();
    }

}
