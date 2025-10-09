package com.eternalcode.example.bukkit.command;

import com.eternalcode.example.bukkit.multification.YourMultification;
import com.eternalcode.multification.packetevents.notice.PacketEventsNotice;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;

import java.time.Duration;

@Command(name = "testadvancements")
@Permission("example.testadvancements")
public class AdvancementCommand {

    private final YourMultification multification;

    public AdvancementCommand(YourMultification multification) {
        this.multification = multification;
    }

    @Execute(name = "simple")
    void executeSimple(@Context Player player) {
        this.multification.create()
                .viewer(player)
                .notice(PacketEventsNotice.advancement(
                        "<green>Simple Achievement",
                        "You triggered a test advancement!"
                ))
                .send();
    }

    @Execute(name = "challenge")
    void executeChallenge(@Context Player player) {
        this.multification.create()
                .viewer(player)
                .notice(PacketEventsNotice.advancement(
                        "<dark_purple>Epic Challenge",
                        "Hold down Left Button",
                        "DIAMOND_SWORD",
                        AdvancementType.CHALLENGE,
                        Duration.ofSeconds(5)
                ))
                .send();
    }

    @Execute(name = "goal")
    void executeGoal(@Context Player player) {
        this.multification.create()
                .viewer(player)
                .notice(PacketEventsNotice.advancement(
                        "<gold>Reach the Goal",
                        "Destroy the tree",
                        "OAK_SAPLING",
                        AdvancementType.GOAL,
                        "minecraft:textures/gui/advancements/backgrounds/stone.png"
                ))
                .send();
    }

    @Execute(name = "custom")
    void executeCustom(@Context Player player) {
        this.multification.create()
                .viewer(player)
                .notice(PacketEventsNotice.builder()
                        .advancement(
                                "<red>Custom Toast",
                                "<gray>With all options configured",
                                "GOLD_INGOT",
                                AdvancementType.TASK,
                                "minecraft:textures/gui/advancements/backgrounds/adventure.png",
                                true,  // showToast
                                true,  // hidden
                                0.0f,  // x
                                0.0f,  // y
                                Duration.ofSeconds(3)  // showTime
                        )
                        .build())
                .send();
    }

    @Execute(name = "translated")
    void executeTranslated(@Context Player player) {
        this.multification.create()
                .viewer(player)
                .notice(messages -> messages.welcomeAdvancement)
                .placeholder("{player}", player.getName())
                .send();
    }

    @Execute(name = "all")
    void executeAll(@Context Player player) {
        executeSimple(player);

        this.multification.create()
                .viewer(player)
                .notice(PacketEventsNotice.advancement(
                        "<yellow>Wait for it...",
                        "More coming soon!",
                        "CLOCK",
                        AdvancementType.TASK,
                        Duration.ofSeconds(2)
                ))
                .send();

        // Schedule next ones
        org.bukkit.Bukkit.getScheduler().runTaskLater(
                org.bukkit.Bukkit.getPluginManager().getPlugin("ExamplePlugin"),
                () -> executeChallenge(player),
                40L // 2 seconds
        );

        org.bukkit.Bukkit.getScheduler().runTaskLater(
                org.bukkit.Bukkit.getPluginManager().getPlugin("ExamplePlugin"),
                () -> executeGoal(player),
                80L
        );

        org.bukkit.Bukkit.getScheduler().runTaskLater(
                org.bukkit.Bukkit.getPluginManager().getPlugin("ExamplePlugin"),
                () -> executeCustom(player),
                120L // 6 seconds
        );
    }
}