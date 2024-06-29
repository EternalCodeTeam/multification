package com.eternalcode.multification.notice.resolver.title;

import com.eternalcode.multification.notice.NoticeType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.title.TitlePart;

public class TitleWithEmptySubtitleResolver extends AbstractTitleContentResolver {

    public TitleWithEmptySubtitleResolver() {
        super(NoticeType.TITLE_WITH_EMPTY_SUBTITLE.toNoticeKey());
    }

    @Override
    public void send(Audience audience, ComponentSerializer<Component, Component, String> componentSerializer, TitleContent content) {
        audience.sendTitlePart(TitlePart.TITLE, componentSerializer.deserialize(content.content()));
        audience.sendTitlePart(TitlePart.SUBTITLE, Component.empty());
    }

}
