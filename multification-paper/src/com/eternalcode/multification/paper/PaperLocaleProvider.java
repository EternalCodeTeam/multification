package com.eternalcode.multification.paper;

import com.eternalcode.multification.locate.LocaleProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;
import org.jspecify.annotations.NonNull;

class PaperLocaleProvider implements LocaleProvider<CommandSender> {

    @Override
    public @NonNull Locale provide(CommandSender viewer) {
        if (viewer instanceof Player player) {
            return player.locale();
        }

        return Locale.ROOT;
    }
}
