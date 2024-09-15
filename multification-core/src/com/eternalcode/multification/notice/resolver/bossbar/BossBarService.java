package com.eternalcode.multification.notice.resolver.bossbar;

import net.kyori.adventure.bossbar.BossBar;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BossBarService {

    private final Map<UUID, BossBarEntity> bossBars = new ConcurrentHashMap<>();
    private final Map<BossBar, Instant> bossBarExpiration = new ConcurrentHashMap<>();

    void add(BossBar bossBar, Duration duration) {
        UUID uuid = UUID.randomUUID();

        this.bossBars.put(uuid, new BossBarEntity(uuid, bossBar, duration, Instant.now().plus(duration)));
        this.bossBarExpiration.put(bossBar, Instant.now().plus(duration));
    }

    void remove(UUID uuid) {
        this.bossBars.remove(uuid);
    }

    boolean hasBossBar(UUID uuid) {
        return this.bossBars.containsKey(uuid);
    }

    Optional<BossBarEntity> getBossBar(UUID uuid) {
        return Optional.ofNullable(this.bossBars.get(uuid));
    }

    Collection<BossBarEntity> getBossBars() {
        return Collections.unmodifiableCollection(this.bossBars.values());
    }

}
