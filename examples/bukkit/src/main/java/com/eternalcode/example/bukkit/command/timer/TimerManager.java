package com.eternalcode.example.bukkit.command.timer;

import com.eternalcode.example.bukkit.multification.YourMultification;
import java.time.Duration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class TimerManager {

    private final BukkitScheduler bukkitScheduler;
    private final Plugin plugin;
    private final YourMultification multification;

    public TimerManager(BukkitScheduler bukkitScheduler, Plugin plugin, YourMultification multification) {
        this.bukkitScheduler = bukkitScheduler;
        this.plugin = plugin;
        this.multification = multification;
    }

    public void startTimer(Duration duration) {
        tickTimer(duration.toSeconds());
    }

    private void tickTimer(long seconds) {
        if (seconds == 0) {
            return;
        }

        this.multification.create()
            .all()
            .notice(messages -> messages.bossbarTimer)
            .placeholder("{time}", String.valueOf(seconds))
            .send();

        this.bukkitScheduler.runTaskLater(plugin, () -> tickTimer(seconds - 1), 20L);
    }

}
