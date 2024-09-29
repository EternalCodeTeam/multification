package com.eternalcode.example.bukkit.config;

import com.eternalcode.multification.bukkit.notice.BukkitNotice;
import com.eternalcode.multification.notice.Notice;
import net.dzikoysk.cdn.entity.Description;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

import java.time.Duration;

public class MessagesConfig {
    @Description("# Fly command message")
    public Notice selfFlyCommandMessage = Notice.chat("<green>You have toggled fly mode. It is now <yellow>{state}</yellow>.");
    public Notice otherFlyCommandMessage = Notice.chat("<green>You have toggled fly mode for <yellow>{player}</yellow>. It is now <yellow>{state}</yellow>.");
    public Notice otherFlyCommandMessageTarget = BukkitNotice.builder()
        .chat("<green>Your fly mode has been toggled by <yellow>{player}</yellow>. It is now <yellow>{state}</yellow>.")
        .sound(Sound.ENTITY_ITEM_PICKUP)
        .build();

    @Description("# Give command message")
    public Notice senderGiveCommandMessage = Notice.title("<green>You have given <yellow>{amount}x {item}</yellow> to <yellow>{player}</yellow>.");
    public Notice receiverGiveCommandMessage = BukkitNotice.builder()
        .title("<green>You have received <yellow>{amount}x {item}</yellow> from <yellow>{player}</yellow>.")
        .subtitle("<pride:gay>Remember to say thank you!</pride>")
        .sound(Sound.ENTITY_ITEM_PICKUP)
        .build();

    @Description("# Teleport command message")
    public Notice selfTeleportCommandMessage = BukkitNotice.builder()
        .actionBar("<green>You have been teleported to <yellow>{player}</yellow>.")
        .sound(Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 2.0F, 2.0F)
        .build();

    @Description("# TO REMOVE?????")
    public Notice bossBarMessage = Notice.builder()
        .bossBar(BossBar.Color.PINK, BossBar.Overlay.PROGRESS, Duration.ofSeconds(5), -1, "<green>Example boss bar message")
        .build();

    @Description("# Epic bossbar timer!!!")
    public Notice bossbarTimer = Notice.builder()
        .bossBar(BossBar.Color.RED, BossBar.Overlay.PROGRESS, Duration.ofSeconds(1), 0.5, "<red>Timer: <yellow>{time}")
        .build();


}
