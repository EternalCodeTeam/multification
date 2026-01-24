package com.eternalcode.example;

import com.eternalcode.example.notice.ExampleMultification;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;

public class PlayerConnectListener {

    private final ExampleMultification multification;

    public PlayerConnectListener(ExampleMultification multification) {
        this.multification = multification;
    }

    @Subscribe
    void onPlayerConnect(ServerConnectedEvent event) {
        this.multification.create()
                .all()
                .notice(messagesConfig -> messagesConfig.joinedTheServer)
                .placeholder("<server>", event.getServer().getServerInfo().getName())
                .send();
    }

}
