package com.eternalcode.example.bukkit.multification;

import com.eternalcode.multification.adventure.AudienceConverter;
import com.eternalcode.example.bukkit.BukkitMultification;
import com.eternalcode.multification.translation.TranslationProvider;
import com.eternalcode.example.bukkit.config.MessagesConfig;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class YourMultification extends BukkitMultification<MessagesConfig> {

    private final MessagesConfig messagesConfig;
    private final AudienceProvider audienceProvider;
    private final MiniMessage miniMessage;

    public YourMultification(MessagesConfig messagesConfig, AudienceProvider audienceProvider, MiniMessage miniMessage) {
        this.messagesConfig = messagesConfig;
        this.audienceProvider = audienceProvider;
        this.miniMessage = miniMessage;
    }

    @Override
    protected @NotNull TranslationProvider<MessagesConfig> translationProvider() {
        return locale -> this.messagesConfig;
    }

    @Override
    protected @NotNull ComponentSerializer<Component, Component, String> serializer() {
        return this.miniMessage;
    }

    @Override
    protected @NotNull AudienceConverter<CommandSender> audienceConverter() {
        return  commandSender -> {
            if (commandSender instanceof Player player) {
                return this.audienceProvider.player(player.getUniqueId());
            }

            return this.audienceProvider.console();
        };
    }

}
