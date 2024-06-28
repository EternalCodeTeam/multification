package com.eternalcode.multification.notice.resolver.title;

import com.eternalcode.multification.notice.NoticeKey;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.title.TitlePart;

public class TitleResolver extends AbstractTitleContentResolver {

    public TitleResolver() {
        super(NoticeKey.TITLE);
    }

    @Override
    public void send(Audience audience, ComponentSerializer<Component, Component, String> componentSerializer, TitleContent content) {
        audience.sendTitlePart(TitlePart.TITLE, componentSerializer.deserialize(content.content()));
    }

}
