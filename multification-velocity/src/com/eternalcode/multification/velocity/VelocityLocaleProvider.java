package com.eternalcode.multification.velocity;

import com.eternalcode.multification.locate.LocaleProvider;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class VelocityLocaleProvider implements LocaleProvider<CommandSource> {

    @Override
    public @NotNull Locale provide(CommandSource commandSource) {
        if (commandSource instanceof Player player) {
            return player.getEffectiveLocale() != null ? player.getEffectiveLocale() : Locale.ROOT;
        }
        return Locale.ROOT;
    }
}
