package com.eternalcode.multification.notice.resolver.actionbar;

import com.eternalcode.multification.notice.NoticeKey;
import com.eternalcode.multification.notice.resolver.NoticeSerdesResult;
import com.eternalcode.multification.notice.resolver.text.TextContentResolver;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

public class ActionbarResolver implements TextContentResolver<ActionbarContent> {

    @Override
    public NoticeKey<ActionbarContent> noticeKey() {
        return NoticeKey.ACTION_BAR;
    }

    @Override
    public void send(Audience audience, ComponentSerializer<Component, Component, String> componentSerializer, ActionbarContent content) {
        audience.sendActionBar(componentSerializer.deserialize(content.content()));
    }

    @Override
    public ActionbarContent applyText(ActionbarContent content, UnaryOperator<String> function) {
        return new ActionbarContent(function.apply(content.content()));
    }

    @Override
    public NoticeSerdesResult serialize(ActionbarContent content) {
        return new NoticeSerdesResult.Single(content.content());
    }

    @Override
    public Optional<ActionbarContent> deserialize(NoticeSerdesResult result) {
        Optional<String> firstElement = result.firstElement();

        return firstElement
            .map(ActionbarContent::new);
    }

    @Override
    public ActionbarContent createFromText(List<String> contents) {
        if (contents.isEmpty()) {
            return new ActionbarContent("");
        }

        return new ActionbarContent(String.join(" ", contents));
    }

}
