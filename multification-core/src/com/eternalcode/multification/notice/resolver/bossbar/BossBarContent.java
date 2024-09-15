package com.eternalcode.multification.notice.resolver.bossbar;

import com.eternalcode.multification.notice.resolver.text.TextContent;
import net.kyori.adventure.bossbar.BossBar;

import java.time.Duration;
import java.util.List;

public record BossBarContent(String content, double progress, BossBar.Color color, BossBar.Overlay overlay, Duration duration) implements TextContent {

    @Override
    public List<String> contents() {
        return List.of(this.content);
    }
}
