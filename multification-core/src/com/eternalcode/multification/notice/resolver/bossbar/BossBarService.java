package com.eternalcode.multification.notice.resolver.bossbar;

import com.eternalcode.commons.scheduler.Scheduler;
import com.eternalcode.commons.scheduler.Task;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

import java.time.Duration;
import java.time.Instant;

public class BossBarService {

    private final Duration REFRESH_DURATION = Duration.ofMillis(500);
    private final Scheduler scheduler;

    public BossBarService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    void sendBossBar(
            ComponentSerializer<Component, Component, String> serializer,
            BossBarContent content,
            Audience viewer
    ) {
        BossBar bossBar = BossBar.bossBar(serializer.deserialize(content.message()), (float) content.progress().orElse(1.0), content.color(), content.overlay());
        viewer.showBossBar(bossBar);

        Duration duration = content.duration();

        Instant now = Instant.now();
        Instant expiration = now.plus(duration);

        if (content.progress().isPresent()) {
            this.scheduler.laterSync(() -> viewer.hideBossBar(bossBar), duration);
            return;
        }

        Task task = this.scheduler.timerSync(() -> {
            Duration remaining = Duration.between(Instant.now(), expiration);
            float progress = 1 - (float) remaining.getSeconds() / duration.getSeconds();

            bossBar.progress(progress);
        }, Duration.ofMillis(10L), REFRESH_DURATION);

        this.scheduler.laterSync(() -> {
            viewer.hideBossBar(bossBar);

            task.cancel();
        }, duration);
    }

}
