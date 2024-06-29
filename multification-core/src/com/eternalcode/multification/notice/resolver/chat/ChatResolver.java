package com.eternalcode.multification.notice.resolver.chat;

import com.eternalcode.multification.notice.NoticeKey;
import com.eternalcode.multification.notice.resolver.NoticeSerdesResult;
import com.eternalcode.multification.notice.resolver.NoticeSerdesResult.Multiple;
import com.eternalcode.multification.notice.resolver.text.TextContentResolver;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

public class ChatResolver implements TextContentResolver<ChatContent> {

    private final NoticeKey<ChatContent> key;

    public ChatResolver() {
        this.key = NoticeKey.CHAT;
    }

    @Override
    public NoticeKey<ChatContent> noticeKey() {
        return key;
    }

    @Override
    public void send(Audience audience, ComponentSerializer<Component, Component, String> componentSerializer, ChatContent content) {
        for (String message : content.messages()) {
            audience.sendMessage(componentSerializer.deserialize(message));
        }
    }

    @Override
    public ChatContent applyText(ChatContent content, UnaryOperator<String> function) {
        return new ChatContent(content.messages().stream()
            .map(function)
            .toList()
        );
    }

    @Override
    public NoticeSerdesResult serialize(ChatContent content) {
        List<String> messages = content.messages();

        if (messages.isEmpty()) {
            return new NoticeSerdesResult.Empty();
        }

        if (messages.size() == 1) {
            return new NoticeSerdesResult.Single(messages.get(0));
        }

        return new Multiple(messages);
    }

    @Override
    public Optional<ChatContent> deserialize(NoticeSerdesResult result) {
        List<String> messages = result.anyElements();

        if (messages.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new ChatContent(messages));
    }

    @Override
    public ChatContent createFromText(List<String> contents) {
        return new ChatContent(contents);
    }
}
