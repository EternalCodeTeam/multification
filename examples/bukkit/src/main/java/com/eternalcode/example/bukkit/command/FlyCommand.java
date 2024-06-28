package com.eternalcode.example.bukkit.command;

import com.eternalcode.example.bukkit.multification.YourMultification;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "fly", aliases = {"f"})
@Permission("example.fly")
public class FlyCommand {

    private final YourMultification multification;

    public FlyCommand(YourMultification multification) {
        this.multification = multification;
    }

    @Execute
    void execute(@Context Player sender) {
        sender.setAllowFlight(!sender.getAllowFlight());
        multification.create()
            .player(sender.getUniqueId())
            .notice(messages -> messages.selfFlyCommandMessage)
            .placeholder("{state}", sender.getAllowFlight() ? "enabled" : "disabled")
            .send();
    }

    @Execute
    void executeOther(@Context CommandSender sender, @Arg Player target) {
        target.setAllowFlight(!target.getAllowFlight());

        multification.create()
            .player(target.getUniqueId())
            .notice(messages -> messages.selfFlyCommandMessage)
            .placeholder("{state}", target.getAllowFlight() ? "enabled" : "disabled")
            .send();

        multification.create()
            .viewer(sender)
            .notice(messages -> messages.otherFlyCommandMessage)
            .placeholder("{state}", target.getAllowFlight() ? "enabled" : "disabled")
            .placeholder("{player}", target.getName())
            .send();

        multification.create()
            .player(target.getUniqueId())
            .notice(messages -> messages.otherFlyCommandMessageTarget)
            .placeholder("{state}", target.getAllowFlight() ? "enabled" : "disabled")
            .placeholder("{player}", sender.getName())
            .send();
    }

}
