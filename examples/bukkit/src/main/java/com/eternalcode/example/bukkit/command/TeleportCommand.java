package com.eternalcode.example.bukkit.command;

import com.eternalcode.example.bukkit.multification.YourMultification;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.annotations.command.Command;
import org.bukkit.entity.Player;

@Command(name = "teleport", aliases = "tp")
@Permission("dev.rollczi.teleport")
public class TeleportCommand {

    private final YourMultification multification;

    public TeleportCommand(YourMultification multification) {
        this.multification = multification;
    }

    @Execute(name = "bossbar")
    public void testBossBar() {
        multification.create()
            .notice(messages -> messages.bossBarMessage)
            .send();
    }

    @Execute
    public void teleportSelf(@Context Player sender, @Arg Player to) {
        multification.create()
            .player(sender.getUniqueId())
            .notice(messages -> messages.selfTeleportCommandMessage)
            .placeholder("{player}", to.getName())
            .send();

        sender.teleport(to.getLocation());
    }

}
