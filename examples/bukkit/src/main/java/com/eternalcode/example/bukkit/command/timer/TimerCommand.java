package com.eternalcode.example.bukkit.command.timer;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import java.time.Duration;

@Command(name = "timer")
@Permission("example.timer")
public class TimerCommand {

    private final TimerManager timerManager;

    public TimerCommand(TimerManager timerManager) {
        this.timerManager = timerManager;
    }

    @Execute
    public void execute() {
        this.timerManager.startTimer(Duration.ofSeconds(5));
    }
}
