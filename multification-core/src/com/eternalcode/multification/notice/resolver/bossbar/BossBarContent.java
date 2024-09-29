package com.eternalcode.multification.notice.resolver.bossbar;

import com.eternalcode.multification.notice.resolver.text.TextContent;
import java.util.List;
import net.kyori.adventure.bossbar.BossBar;

import java.time.Duration;
import java.util.OptionalDouble;

public record BossBarContent(BossBar.Color color, BossBar.Overlay overlay, Duration duration, OptionalDouble progress, String message) implements TextContent {

    @Override
    public List<String> contents() {
        return List.of(message);
    }

}
