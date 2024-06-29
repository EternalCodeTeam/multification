package com.eternalcode.multification.notice.resolver.title;

import com.eternalcode.multification.notice.NoticeKey;
import com.eternalcode.multification.notice.resolver.NoticeResolver;
import com.eternalcode.multification.notice.resolver.NoticeSerdesResult;
import java.util.Optional;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

public class TitleHideResolver implements NoticeResolver<TitleHide> {

    @Override
    public NoticeKey<TitleHide> noticeKey() {
        return NoticeKey.TITLE_HIDE;
    }

    @Override
    public void send(Audience audience, ComponentSerializer<Component, Component, String> componentSerializer, TitleHide content) {
        if (content.isHided()) {
            audience.clearTitle();
        }
    }

    @Override
    public NoticeSerdesResult serialize(TitleHide content) {
        return new NoticeSerdesResult.Single(String.valueOf(content.isHided()));
    }

    @Override
    public Optional<TitleHide> deserialize(NoticeSerdesResult result) {
        Optional<String> firstElement = result.firstElement();

        return firstElement
            .map(value -> new TitleHide(Boolean.parseBoolean(value)));
    }

}
