package com.eternalcode.example.paper.command;

import com.eternalcode.example.paper.config.ConfigurationManager;
import com.eternalcode.example.paper.multification.ExampleMultification;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "reload-config")
public class ReloadCommand {

    private final ConfigurationManager configurationManager;
    private final ExampleMultification multification;

    public ReloadCommand(ConfigurationManager configurationManager, ExampleMultification multification) {
        this.configurationManager = configurationManager;
        this.multification = multification;
    }

    @Execute
    public void execute(@Context CommandSender sender) {
        this.configurationManager.reload();

        if (sender instanceof Player player) {
            this.multification.create()
                    .player(player.getUniqueId())
                    .notice(messagesConfig -> messagesConfig.reloadMessage)
                    .send();
            return;
        }

        this.multification.create()
                .console()
                .notice(messagesConfig -> messagesConfig.reloadMessage)
                .send();
    }
}
