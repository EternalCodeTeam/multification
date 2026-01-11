package com.eternalcode.example.paper;

import com.eternalcode.example.paper.command.FlyCommand;
import com.eternalcode.example.paper.command.GiveCommand;
import com.eternalcode.example.paper.command.ReloadCommand;
import com.eternalcode.example.paper.command.TeleportCommand;
import com.eternalcode.example.paper.command.timer.TimerCommand;
import com.eternalcode.example.paper.command.timer.TimerManager;
import com.eternalcode.example.paper.config.ConfigurationManager;
import com.eternalcode.example.paper.config.MessagesConfig;
import com.eternalcode.example.paper.multification.ExampleMultification;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ExamplePaperPlugin extends JavaPlugin {

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        // Config & Multification
        MessagesConfig messagesConfig = new MessagesConfig();
        ExampleMultification multification = new ExampleMultification(messagesConfig);

        ConfigurationManager configurationManager = new ConfigurationManager(this.getDataFolder(),
                multification.getNoticeRegistry());
        configurationManager.load(messagesConfig, "messages.yml");

        this.liteCommands = LiteBukkitFactory.builder()
                .commands(
                        new TeleportCommand(multification),
                        new FlyCommand(multification),
                        new GiveCommand(multification),
                        new ReloadCommand(configurationManager, multification),
                        new TimerCommand(new TimerManager(this.getServer().getScheduler(), this, multification)))
                .build();
    }

    @Override
    public void onDisable() {
        if (this.liteCommands != null) {
            this.liteCommands.unregister();
        }
    }
}
