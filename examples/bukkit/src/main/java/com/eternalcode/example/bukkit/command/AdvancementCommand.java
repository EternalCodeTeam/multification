package com.eternalcode.example.bukkit.command;

import com.eternalcode.example.bukkit.multification.YourMultification;
import com.eternalcode.multification.notice.Notice;
import com.eternalcode.multification.bukkit.notice.resolver.advancement.PacketEventsNotice;
import com.eternalcode.multification.bukkit.notice.resolver.advancement.AdvancementFrameType;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.time.Duration;

@Command(name = "testadvancements")
@Permission("example.testadvancements")
public class AdvancementCommand {

    private final YourMultification multification;
    private final Plugin plugin;

    public AdvancementCommand(YourMultification multification, Plugin plugin) {
        this.multification = multification;
        this.plugin = plugin;
    }

    @Execute(name = "simple")
    void executeSimple(@Context Player player) {
        // Using simple helper method
        this.multification.create()
                .viewer(player)
                .notice(PacketEventsNotice.advancement(
                        "<green>Simple Achievement",
                        "You triggered a test advancement!"
                ))
                .send();
    }

    @Execute(name = "withicon")
    void executeWithIcon(@Context Player player) {
        // Using helper method with icon
        this.multification.create()
                .viewer(player)
                .notice(PacketEventsNotice.advancement(
                        "<yellow>Achievement Unlocked",
                        "You found a diamond!",
                        "DIAMOND"
                ))
                .send();
    }

    @Execute(name = "challenge")
    void executeChallenge(@Context Player player) {
        // Using helper method with frame type
        this.multification.create()
                .viewer(player)
                .notice(PacketEventsNotice.advancement(
                        "<dark_purple>Epic Challenge",
                        "Defeat the Ender Dragon",
                        "DRAGON_HEAD",
                        AdvancementFrameType.CHALLENGE
                ))
                .send();
    }

    @Execute(name = "goal")
    void executeGoal(@Context Player player) {
        // Using builder for more customization
        this.multification.create()
                .viewer(player)
                .notice(PacketEventsNotice.builder()
                        .title("<gold>Reach the Goal")
                        .description("Plant 100 trees")
                        .icon("OAK_SAPLING")
                        .frameType(AdvancementFrameType.GOAL)
                        .background("minecraft:textures/gui/advancements/backgrounds/stone.png")
                        .buildAdvancement())
                .send();
    }

    @Execute(name = "timed")
    void executeTimed(@Context Player player) {
        // Using builder with show time
        this.multification.create()
                .viewer(player)
                .notice(PacketEventsNotice.builder()
                        .title("<aqua>Quick Message")
                        .description("This will disappear in 3 seconds")
                        .icon("CLOCK")
                        .frameType(AdvancementFrameType.TASK)
                        .showTime(Duration.ofSeconds(3))
                        .buildAdvancement())
                .send();
    }

    @Execute(name = "custom")
    void executeCustom(@Context Player player) {
        // Using builder with full customization
        this.multification.create()
                .viewer(player)
                .notice(PacketEventsNotice.builder()
                        .title("<red>Custom Toast")
                        .description("<gray>With all options configured")
                        .icon("GOLD_INGOT")
                        .frameType(AdvancementFrameType.TASK)
                        .background("minecraft:textures/gui/advancements/backgrounds/adventure.png")
                        .position(0.0f, 0.0f)
                        .showTime(Duration.ofSeconds(5))
                        .showToast(true)
                        .hidden(true)
                        .buildAdvancement())
                .send();
    }

    @Execute(name = "positioned")
    void executePositioned(@Context Player player) {
        // Custom position example
        this.multification.create()
                .viewer(player)
                .notice(PacketEventsNotice.builder()
                        .title("<blue>Centered Message")
                        .description("This appears in the center")
                        .icon("COMPASS")
                        .position(0.5f, 0.5f)
                        .buildAdvancement())
                .send();
    }

    @Execute(name = "translated")
    void executeTranslated(@Context Player player) {
        // Chat message with placeholder
        this.multification.create()
                .viewer(player)
                .notice(Notice.chat("<green>Hello, {player}! This is a translated message."))
                .placeholder("{player}", player.getName())
                .send();
    }

    @Execute(name = "all")
    void executeAll(@Context Player player) {
        // Show simple first
        executeSimple(player);

        // Schedule more advancements
        Bukkit.getScheduler().runTaskLater(
                this.plugin,
                () -> executeWithIcon(player),
                40L // 2 seconds
        );

        Bukkit.getScheduler().runTaskLater(
                this.plugin,
                () -> executeChallenge(player),
                80L // 4 seconds
        );

        Bukkit.getScheduler().runTaskLater(
                this.plugin,
                () -> executeGoal(player),
                120L // 6 seconds
        );

        Bukkit.getScheduler().runTaskLater(
                this.plugin,
                () -> executeCustom(player),
                160L // 8 seconds
        );
    }
}