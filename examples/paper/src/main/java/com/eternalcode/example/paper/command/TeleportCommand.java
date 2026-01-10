package com.eternalcode.example.paper.command;

import com.eternalcode.example.paper.multification.ExampleMultification;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.annotations.command.Command;
import org.bukkit.entity.Player;

@Command(name = "teleport", aliases = "tp")
@Permission("example.teleport")
public class TeleportCommand {

    private final ExampleMultification multification;

    public TeleportCommand(ExampleMultification multification) {
        this.multification = multification;
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
