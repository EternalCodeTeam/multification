package com.eternalcode.multification.notice.resolver.title;

import com.eternalcode.multification.notice.NoticeKey;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.title.TitlePart;

public class SubtitleResolver extends AbstractTitleContentResolver {

    public SubtitleResolver() {
        super(NoticeKey.SUBTITLE);
    }

    @Override
    public void send(Audience audience, ComponentSerializer<Component, Component, String> componentSerializer, TitleContent content) {
        audience.sendTitlePart(TitlePart.SUBTITLE, componentSerializer.deserialize(content.content()));
    }

}
