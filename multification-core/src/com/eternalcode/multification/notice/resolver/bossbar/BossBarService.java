package com.eternalcode.multification.notice.resolver.bossbar;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BossBarService {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(8);

    void sendBossBar(
            ComponentSerializer<Component, Component, String> serializer,
            BossBarContent content,
            Audience viewer
    ) {
        BossBar bossBar = BossBar.bossBar(serializer.deserialize(content.message()), (float) content.progress().orElse(1.0), content.color(), content.overlay().orElse(BossBar.Overlay.PROGRESS));
        viewer.showBossBar(bossBar);

        Duration duration = content.duration();

        Instant now = Instant.now();
        Instant expiration = now.plus(duration);

        if (content.progress().isEmpty()) {
            updateProgress(expiration, duration, bossBar, viewer);
        }

        this.scheduler.schedule(() -> viewer.hideBossBar(bossBar), duration.toMillis(), TimeUnit.MILLISECONDS);
    }

    private void updateProgress(Instant expiration, Duration duration, BossBar bossBar, Audience viewer) {
        if (Instant.now().isAfter(expiration)) {
            viewer.hideBossBar(bossBar);
            return;
        }

        Duration remaining = Duration.between(Instant.now(), expiration);
        double totalMillis = duration.toMillis();
        double remainingMillis = Math.max(0, remaining.toMillis());
        float progress = (float) (remainingMillis / totalMillis);
        progress = Math.max(0.0f, Math.min(1.0f, progress));

        bossBar.progress(progress);

        this.scheduler.schedule(() -> updateProgress(expiration, duration, bossBar, viewer), 100L, TimeUnit.MILLISECONDS);
    }

}
