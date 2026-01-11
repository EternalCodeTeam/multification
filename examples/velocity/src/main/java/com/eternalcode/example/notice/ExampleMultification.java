package com.eternalcode.example.notice;


import com.eternalcode.example.config.MessagesConfig;
import com.eternalcode.multification.adventure.AudienceConverter;
import com.eternalcode.multification.translation.TranslationProvider;
import com.eternalcode.multification.velocity.VelocityMultification;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class ExampleMultification extends VelocityMultification<MessagesConfig> {

    private final MessagesConfig messagesConfig;
    private final MiniMessage miniMessage;

    public ExampleMultification(Plugin plugin, ProxyServer server, MessagesConfig messagesConfig) {
        super(server, plugin);
        this.messagesConfig = messagesConfig;
        this.miniMessage = MiniMessage.miniMessage();
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
    protected @NotNull AudienceConverter<CommandSource> audienceConverter() {
        return commandSender -> commandSender;
    }
}
