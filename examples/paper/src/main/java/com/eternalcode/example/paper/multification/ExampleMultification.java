package com.eternalcode.example.paper.multification;

import com.eternalcode.example.paper.config.MessagesConfig;
import com.eternalcode.multification.adventure.AudienceConverter;
import com.eternalcode.multification.paper.PaperMultification;
import com.eternalcode.multification.translation.TranslationProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ExampleMultification extends PaperMultification<MessagesConfig> {

    private final MessagesConfig messagesConfig;
    private final MiniMessage miniMessage;

    public ExampleMultification(MessagesConfig messagesConfig) {
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
    protected @NotNull AudienceConverter<CommandSender> audienceConverter() {
        return commandSender -> commandSender;
    }
}
