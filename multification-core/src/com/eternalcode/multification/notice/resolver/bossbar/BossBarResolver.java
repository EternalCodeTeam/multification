package com.eternalcode.multification.notice.resolver.bossbar;

import com.eternalcode.multification.notice.NoticeKey;
import com.eternalcode.multification.notice.resolver.NoticeResolver;
import com.eternalcode.multification.notice.resolver.NoticeSerdesResult;
import com.eternalcode.multification.notice.resolver.text.TextContentResolver;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class BossBarResolver implements NoticeResolver<BossBarContent> {

    private static final String BOSSBAR_WITH_FLOAT = "%s %s %s %s %s";
    private static final String BOSSBAR_WITHOUT_FLOAT = "%s %s %s %s";

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
        if (content.progress().isEmpty()) {
            return new NoticeSerdesResult.Single(String.format(BOSSBAR_WITHOUT_FLOAT,
                content.color().name(),
                content.overlay().name(),
                content.duration().toString(),
                content.message()
            ));
        }

        return new NoticeSerdesResult.Single(String.format(BOSSBAR_WITH_FLOAT,
                content.color().name(),
                content.overlay().name(),
                content.duration().toString(),
                content.progress().getAsDouble(),
                content.message()
        ));
    }

    @Override
    public Optional<BossBarContent> deserialize(NoticeSerdesResult result) {
        Optional<String> firstElementOptional = result.firstElement();

        if (firstElementOptional.isEmpty()) {
            return Optional.empty();
        }

        String rawContent = firstElementOptional.get();

        String[] bossBar = rawContent.split(" ");
        List<String> bossBarList = List.of(bossBar);

        if (bossBarList.size() < 4) {
            throw new IllegalArgumentException("BossBar content is not valid: " + rawContent);
        }

        BossBar.Color color = BossBar.Color.valueOf(bossBarList.get(0));
        BossBar.Overlay overlay = BossBar.Overlay.valueOf(bossBarList.get(1));
        Duration duration = Duration.parse(bossBarList.get(2));
        OptionalDouble progress = this.parseProgress(bossBarList);
        String message = String.join(" ", bossBarList.subList(progress.isPresent() ? 4 : 3, bossBarList.size()));

        return Optional.of(new BossBarContent(color, overlay, duration, progress, message));
    }

    OptionalDouble parseProgress(List<String> bossBarList) {
        if (bossBarList.size() <= 4) {
            return OptionalDouble.empty();
        }
        try {
            return OptionalDouble.of(Double.parseDouble(bossBarList.get(3)));
        }
        catch (NumberFormatException e) {
            return OptionalDouble.empty();
        }
    }
}
