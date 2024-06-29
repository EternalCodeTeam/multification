package com.eternalcode.multification.notice.resolver;

import com.eternalcode.multification.notice.NoticeKey;
import java.util.Optional;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

public interface NoticeResolver<T extends NoticeContent> {

    NoticeKey<T> noticeKey();

    void send(Audience audience, ComponentSerializer<Component, Component, String> componentSerializer, T content);

    NoticeSerdesResult serialize(T content);

    Optional<T> deserialize(NoticeSerdesResult result);

}
