package com.eternalcode.multification.platform;

import com.eternalcode.multification.notice.resolver.NoticeResolverRegistry;
import com.eternalcode.multification.notice.resolver.NoticeContent;
import com.eternalcode.multification.notice.NoticePart;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

class PlatformBroadcasterImpl implements PlatformBroadcaster {

    private final ComponentSerializer<Component, Component, String> componentSerializer;
    private final NoticeResolverRegistry noticeRegistry;

    public PlatformBroadcasterImpl(ComponentSerializer<Component, Component, String> componentSerializer, NoticeResolverRegistry noticeRegistry) {
        this.componentSerializer = componentSerializer;
        this.noticeRegistry = noticeRegistry;
    }

    @Override
    public <T extends NoticeContent> void announce(Audience audience, NoticePart<T> part) {
        noticeRegistry.send(audience, componentSerializer, part);
    }

}
