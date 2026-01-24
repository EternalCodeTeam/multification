package com.eternalcode.example.command;

import com.eternalcode.example.notice.ExampleMultification;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;

@Command(name = "switch")
public class SwitchCommand {

    private final ExampleMultification multification;

    public SwitchCommand(ExampleMultification multification) {
        this.multification = multification;
    }

    @Execute
    void execute(@Context Player player, @Arg RegisteredServer server) {
        player.createConnectionRequest(server).fireAndForget();
        this.multification.create()
                .player(player.getUniqueId())
                .notice(messagesConfig -> messagesConfig.switchedServer)
                .placeholder("<server>", server.getServerInfo().getName())
                .send();

    }
}
