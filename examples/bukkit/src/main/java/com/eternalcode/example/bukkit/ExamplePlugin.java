package com.eternalcode.example.bukkit;

import com.eternalcode.example.bukkit.command.GiveCommand;
import com.eternalcode.example.bukkit.command.TeleportCommand;
import com.eternalcode.example.bukkit.command.timer.TimerCommand;
import com.eternalcode.example.bukkit.command.timer.TimerManager;
import com.eternalcode.example.bukkit.config.ConfigurationManager;
import com.eternalcode.example.bukkit.config.MessagesConfig;
import com.eternalcode.example.bukkit.multification.YourMultification;
import com.eternalcode.example.bukkit.command.FlyCommand;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.LiteCommands;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ExamplePlugin extends JavaPlugin {

    private LiteCommands<CommandSender> liteCommands;
    private AudienceProvider audienceProvider;

    @Override
    public void onEnable() {
        // Kyori Adventure platform
        audienceProvider = BukkitAudiences.create(this);
        MiniMessage miniMessage = MiniMessage.miniMessage();

        // Config & Multification
        MessagesConfig messagesConfig = new MessagesConfig();
        YourMultification multification = new YourMultification(messagesConfig, audienceProvider, miniMessage);

        ConfigurationManager configurationManager = new ConfigurationManager(this.getDataFolder(), multification.getNoticeRegistry());
        configurationManager.load(messagesConfig, "messages.yml");

        this.liteCommands = LiteBukkitFactory.builder()
            .commands(
                new TeleportCommand(multification),
                new FlyCommand(multification),
                new GiveCommand(multification),
                new TimerCommand(new TimerManager(this.getServer().getScheduler(), this, multification))
            )
            .build();
    }

    @Override
    public void onDisable() {
        // unregister all commands from bukkit
        if (this.liteCommands != null) {
            this.liteCommands.unregister();
        }

        // close audience provider
        if (this.audienceProvider != null) {
            this.audienceProvider.close();
        }
    }

}
