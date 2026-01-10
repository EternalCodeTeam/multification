package com.eternalcode.example.paper.config;

import com.eternalcode.multification.notice.Notice;
import net.dzikoysk.cdn.entity.Description;
import net.kyori.adventure.bossbar.BossBar;

import java.time.Duration;

public class MessagesConfig {

    @Description("# Fly command message")
    public Notice selfFlyCommandMessage = Notice
            .chat("<green>You have toggled fly mode. It is now <yellow>{state}</yellow>.");
    public Notice otherFlyCommandMessage = Notice.chat(
            "<green>You have toggled fly mode for <yellow>{player}</yellow>. It is now <yellow>{state}</yellow>.");
    public Notice otherFlyCommandMessageTarget = Notice.builder()
            .chat("<green>Your fly mode has been toggled by <yellow>{player}</yellow>. It is now <yellow>{state}</yellow>.")
            .sound("minecraft:entity.item.pickup")
            .build();

    @Description("# Give command message")
    public Notice senderGiveCommandMessage = Notice
            .title("<green>You have given <yellow>{amount}x {item}</yellow> to <yellow>{player}</yellow>.");
    public Notice receiverGiveCommandMessage = Notice.builder()
            .title("<green>You have received <yellow>{amount}x {item}</yellow> from <yellow>{player}</yellow>.")
            .subtitle("<pride:pride>Remember to say thank you!</pride>")
            .sound("minecraft:entity.item.pickup")
            .build();

    @Description("# Teleport command message")
    public Notice selfTeleportCommandMessage = Notice.builder()
            .actionBar("<green>You have been teleported to <yellow>{player}</yellow>.")
            .sound("minecraft:entity.enderman.teleport", 2.0F, 2.0F)
            .build();

    @Description("# Epic bossbar timer!!!")
    public Notice bossbarTimer = Notice.builder()
            .bossBar(BossBar.Color.RED, Duration.ofSeconds(1), "<red>Timer: <yellow>{time}")
            .build();

    public Notice reloadMessage = Notice.builder()
            .chat("<pride:pride>Configuration has been reloaded!")
            .sound("minecraft:ambient.basalt_deltas.additions", 1.0F, 1.0F)
            .build();
}
