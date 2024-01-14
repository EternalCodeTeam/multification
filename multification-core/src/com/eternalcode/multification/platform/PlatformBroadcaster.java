package com.eternalcode.multification.platform;

import com.eternalcode.multification.notice.NoticeContent;
import com.eternalcode.multification.notice.NoticePart;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

public interface PlatformBroadcaster {

    <T extends NoticeContent>
    void announce(Audience viewer, NoticePart<T> notice);

    static PlatformBroadcaster withPlainSerializer() {
        return new PlatformBroadcasterImpl(new PlainComponentSerializer());
    }

    static PlatformBroadcaster withSerializer(ComponentSerializer<Component, Component, String> componentSerializer) {
        return new PlatformBroadcasterImpl(componentSerializer);
    }

}
