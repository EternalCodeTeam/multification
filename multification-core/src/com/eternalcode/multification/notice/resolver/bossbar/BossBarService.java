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

    // Configurable update interval in milliseconds (used for scheduling progress updates)
    private final long updateIntervalMillis;

    // Default constructor: reads system property 'multification.bossbar.update-interval-ms' or falls back to 100ms
    public BossBarService() {
        this(getUpdateIntervalFromSystemProperty());
    }

    // Constructor allowing explicit configuration (useful for tests or programmatic configuration)
    public BossBarService(long updateIntervalMillis) {
        this.updateIntervalMillis = Math.max(1L, updateIntervalMillis);
    }

    private static long getUpdateIntervalFromSystemProperty() {
        final String key = "multification.bossbar.update-interval-ms";
        final long fallback = 100L;
        String value = System.getProperty(key);
        if (value == null) return fallback;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }

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
        double totalMillis = Math.max(0.0, duration.toMillis());
        double remainingMillis = Math.max(0.0, remaining.toMillis());

        float progress;
        if (totalMillis <= 0.0) {
            progress = 0.0f;
        } else {
            double fraction = remainingMillis / totalMillis;
            progress = (float) fraction;
            progress = Math.max(0.0f, Math.min(1.0f, progress));
        }

        bossBar.progress(progress);

        this.scheduler.schedule(() -> updateProgress(expiration, duration, bossBar, viewer), this.updateIntervalMillis, TimeUnit.MILLISECONDS);
    }

}
