package com.eternalcode.multification.platform;

import com.eternalcode.multification.adventure.PlainComponentSerializer;
import com.eternalcode.multification.notice.resolver.NoticeContent;
import com.eternalcode.multification.notice.NoticePart;
import com.eternalcode.multification.notice.resolver.NoticeResolverDefaults;
import com.eternalcode.multification.notice.resolver.NoticeResolverRegistry;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface PlatformBroadcaster {

    <T extends NoticeContent>
    void announce(Audience viewer, NoticePart<T> notice);

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.2.0")
    static PlatformBroadcaster withPlainSerializer() {
        return new PlatformBroadcasterImpl(new PlainComponentSerializer(), NoticeResolverDefaults.createRegistry());
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.2.0")
    static PlatformBroadcaster withSerializer(ComponentSerializer<Component, Component, String> componentSerializer) {
        return new PlatformBroadcasterImpl(componentSerializer, NoticeResolverDefaults.createRegistry());
    }

    @ApiStatus.Internal
    static PlatformBroadcaster create(ComponentSerializer<Component, Component, String> componentSerializer, NoticeResolverRegistry noticeRegistry) {
        return new PlatformBroadcasterImpl(componentSerializer, noticeRegistry);
    }

}
