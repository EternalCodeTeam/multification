package com.eternalcode.multification.notice.resolver.bossbar;

import com.eternalcode.multification.notice.resolver.NoticeContent;
import net.kyori.adventure.bossbar.BossBar;

import java.time.Duration;
import java.util.OptionalDouble;

public record BossBarContent(String message, OptionalDouble progress, BossBar.Color color, BossBar.Overlay overlay, Duration duration) implements NoticeContent {


}
