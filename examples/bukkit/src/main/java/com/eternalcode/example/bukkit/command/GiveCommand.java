package com.eternalcode.example.bukkit.command;

import com.eternalcode.example.bukkit.multification.YourMultification;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Command(name = "give")
public class GiveCommand {

    private final YourMultification multification;

    public GiveCommand(YourMultification multification) {
        this.multification = multification;
    }

    @Execute
    public void execute(
        @Context CommandSender commandSender,
        @Arg("nick") Player target,
        @Arg("item") Material item,
        @OptionalArg("amount") Integer amount
    ) {
        if (amount == null) {
            amount = 1;
        }

        multification.create()
            .player(target.getUniqueId())
            .notice(messages -> messages.receiverGiveCommandMessage)
            .placeholder("{player}", commandSender.getName())
            .placeholder("{amount}", amount.toString())
            .placeholder("{item}", item.name())
            .send();


        multification
            .create()
            .viewer(commandSender)
            .notice(messages -> messages.senderGiveCommandMessage)
            .placeholder("{player}", target.getName())
            .placeholder("{amount}", amount.toString())
            .placeholder("{item}", item.name())
            .send();

        target.getInventory().addItem(new ItemStack(item, amount));
    }

}
