package com.eternalcode.example.config;

import com.eternalcode.multification.notice.Notice;
import net.dzikoysk.cdn.entity.Description;
import net.kyori.adventure.bossbar.BossBar;

import java.time.Duration;

public class MessagesConfig {

    @Description("# Join message")
    public Notice joinedTheServer = Notice.builder()
            .chat("<green><player> has joined the server!")
            .bossBar(
                    BossBar.Color.GREEN,
                    BossBar.Overlay.PROGRESS,
                    Duration.ofSeconds(5),
                    1.0F,
                    "<green><player> has joined the server!"
            )
            .sound("minecraft:entity.player.levelup", 1.0F, 1.0F)
            .build();

    @Description("# Server switch message")
    public Notice switchedServer = Notice.builder()
            .chat("Switched to <server>!")
            .sound("minecraft:entity.enderman.teleport", 1.0F, 1.0F)
            .build();

    public Notice reloadMessage = Notice.builder()
            .chat("<pride:pride>Configuration has been reloaded!")
            .sound("minecraft:ambient.basalt_deltas.additions", 1.0F, 1.0F)
            .build();
}
