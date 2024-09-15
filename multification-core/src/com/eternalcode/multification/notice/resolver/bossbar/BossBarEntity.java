package com.eternalcode.multification.notice.resolver.bossbar;

import net.kyori.adventure.bossbar.BossBar;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public record BossBarEntity(UUID id, BossBar bossBar, Duration duration, Instant expiration) {

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiration);
    }

    public float progress() {
        Duration remaining = Duration.between(Instant.now(), this.expiration);
        return (float) (1 - ((double) remaining.getSeconds() / this.duration.getSeconds()));
    }
}
