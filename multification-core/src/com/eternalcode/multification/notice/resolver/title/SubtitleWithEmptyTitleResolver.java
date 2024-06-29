package com.eternalcode.multification.notice.resolver.title;

import com.eternalcode.multification.notice.NoticeType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.title.TitlePart;

public class SubtitleWithEmptyTitleResolver extends AbstractTitleContentResolver {

    public SubtitleWithEmptyTitleResolver() {
        super(NoticeType.SUBTITLE_WITH_EMPTY_TITLE.toNoticeKey());
    }

    @Override
    public void send(Audience audience, ComponentSerializer<Component, Component, String> componentSerializer, TitleContent content) {
        audience.sendTitlePart(TitlePart.SUBTITLE, componentSerializer.deserialize(content.content()));
        audience.sendTitlePart(TitlePart.TITLE, Component.empty());
    }

}
