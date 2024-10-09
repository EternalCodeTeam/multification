package com.eternalcode.multification.notice.resolver.bossbar;

import com.eternalcode.multification.notice.NoticeKey;
import com.eternalcode.multification.notice.resolver.NoticeSerdesResult;
import com.eternalcode.multification.notice.resolver.text.TextContentResolver;
import com.eternalcode.multification.time.DurationParser;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

import java.time.Duration;
import java.util.Optional;
import java.util.OptionalDouble;

public class BossBarResolver implements TextContentResolver<BossBarContent> {

    private static final String DEFAULT_COLOR = "WHITE";
    private static final String DEFAULT_DURATION = "1s";
    private static final String DEFAULT_MESSAGE = "Default message";

    private final NoticeKey<BossBarContent> key;
    private final BossBarService bossBarService;

    public BossBarResolver(BossBarService bossBarService) {
        this.bossBarService = bossBarService;
        this.key = NoticeKey.BOSS_BAR;
    }

    @Override
    public NoticeKey<BossBarContent> noticeKey() {
        return this.key;
    }

    @Override
    public void send(Audience audience, ComponentSerializer<Component, Component, String> componentSerializer, BossBarContent content) {
        this.bossBarService.sendBossBar(componentSerializer, content, audience);
    }

    @Override
    public NoticeSerdesResult serialize(BossBarContent content) {
        Map<String, String> map = new LinkedHashMap<>();

        map.put("message", content.message());
        map.put("duration", DurationParser.TIME_UNITS.format(content.duration()));
        map.put("color", content.color().name());

        Optional<BossBar.Overlay> overlay = content.overlay();
        overlay.ifPresent(value -> map.put("overlay", String.valueOf(value)));
        OptionalDouble progress = content.progress();
        if (progress.isPresent()) {
            map.put("progress", String.valueOf(progress.getAsDouble()));
        }

        return new NoticeSerdesResult.Section(map);
    }

    @Override
    public Optional<BossBarContent> deserialize(NoticeSerdesResult result) {
        if (!(result instanceof NoticeSerdesResult.Section sectionResult)) {
            return Optional.empty();
        }

        BossBar.Color color = BossBar.Color.valueOf(sectionResult.elements().getOrDefault("color", DEFAULT_COLOR));
        Duration duration = DurationParser.TIME_UNITS.parse(sectionResult.elements().getOrDefault("duration", DEFAULT_DURATION));

        OptionalDouble progress = this.parseProgress(sectionResult.elements().get("progress"));

        Optional<BossBar.Overlay> overlay;

        if (sectionResult.elements().get("overlay") != null) {
            overlay = Optional.of(BossBar.Overlay.valueOf(sectionResult.elements().get("overlay")));
        }
        else {
            overlay = Optional.empty();
        }

        String message = sectionResult.elements().getOrDefault("message", DEFAULT_MESSAGE);

        return Optional.of(new BossBarContent(color, overlay, duration, progress, message));
    }

    OptionalDouble parseProgress(String rawProgress) {
        if (rawProgress == null) {
            return OptionalDouble.empty();
        }

        try {
            double progress = Double.parseDouble(rawProgress);
            return OptionalDouble.of(progress);
        }
        catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid progress value: " + rawProgress, exception);
        }
    }

    @Override
    public BossBarContent createFromText(List<String> contents) {
        throw new UnsupportedOperationException("BossBarResolver does not support creating from text");
    }

    @Override
    public BossBarContent applyText(BossBarContent content, UnaryOperator<String> function) {
        return new BossBarContent(content.color(), content.overlay(), content.duration(), content.progress(), function.apply(content.message()));
    }

}
