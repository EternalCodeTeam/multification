package com.eternalcode.multification.notice.resolver.bossbar;

import com.eternalcode.multification.notice.resolver.NoticeContent;
import net.kyori.adventure.bossbar.BossBar;

import java.time.Duration;
import java.util.OptionalDouble;

public record BossBarContent(BossBar.Color color, BossBar.Overlay overlay, Duration duration, OptionalDouble progress, String message) implements NoticeContent {


}
