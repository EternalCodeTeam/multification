package com.eternalcode.multification.notice.resolver.title;

import com.eternalcode.multification.notice.NoticeKey;
import com.eternalcode.multification.notice.resolver.NoticeResolver;
import com.eternalcode.multification.notice.resolver.NoticeSerdesResult;
import com.eternalcode.multification.time.DurationParser;
import com.eternalcode.multification.time.TemporalAmountParser;
import java.time.Duration;
import java.util.Optional;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;

public class TimesResolver implements NoticeResolver<TitleTimes> {

    private static final TemporalAmountParser<Duration> DURATION_PARSER = DurationParser.TIME_UNITS;
    private static final String TIMES_FORMAT = "%s %s %s";

    @Override
    public NoticeKey<TitleTimes> noticeKey() {
        return NoticeKey.TITLE_TIMES;
    }

    @Override
    public void send(Audience audience, ComponentSerializer<Component, Component, String> componentSerializer, TitleTimes content) {
        audience.sendTitlePart(TitlePart.TIMES, Title.Times.times(
            content.fadeIn(),
            content.stay(),
            content.fadeOut()
        ));
    }

    @Override
    public NoticeSerdesResult serialize(TitleTimes content) {
        return new NoticeSerdesResult.Single(String.format(TIMES_FORMAT,
            DURATION_PARSER.format(content.fadeIn()),
            DURATION_PARSER.format(content.stay()),
            DURATION_PARSER.format(content.fadeOut())
        ));
    }

    @Override
    public Optional<TitleTimes> deserialize(NoticeSerdesResult result) {
        Optional<String> firstElement = result.firstElement();

        if (firstElement.isEmpty()) {
            return Optional.empty();
        }

        String[] parts = firstElement.get().split(" ");

        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid times format " + firstElement.get());
        }

        return Optional.of(new TitleTimes(
            DURATION_PARSER.parse(parts[0]),
            DURATION_PARSER.parse(parts[1]),
            DURATION_PARSER.parse(parts[2])
        ));
    }

}
