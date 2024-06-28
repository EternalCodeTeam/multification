package dev.rollczi.example.bukkit;

import dev.rollczi.example.bukkit.command.GiveCommand;
import dev.rollczi.example.bukkit.command.FlyCommand;
import dev.rollczi.example.bukkit.command.TeleportCommand;
import dev.rollczi.example.bukkit.config.ConfigurationManager;
import dev.rollczi.example.bukkit.config.MessagesConfig;
import dev.rollczi.example.bukkit.multification.YourMultification;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.cooldown.CooldownContext;
import dev.rollczi.litecommands.message.LiteMessages;
import dev.rollczi.litecommands.time.DurationParser;
import java.time.Duration;
import java.time.Instant;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ExamplePlugin extends JavaPlugin {

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        // Kyori Adventure platform
        AudienceProvider audienceProvider = BukkitAudiences.create(this);
        MiniMessage miniMessage = MiniMessage.miniMessage();

        // Config & Multification
        MessagesConfig messagesConfig = new MessagesConfig();
        YourMultification multification = new YourMultification(messagesConfig, audienceProvider, miniMessage);

        ConfigurationManager configurationManager = new ConfigurationManager(this.getDataFolder(), multification.getNoticeRegistry());
        configurationManager.load(messagesConfig, "messages.yml");


        this.liteCommands = LiteBukkitFactory.builder()
            .commands(
                new TeleportCommand(),
                new FlyCommand(),
                new GiveCommand()
            )

            .build();
    }

    @Override
    public void onDisable() {
        // unregister all commands from bukkit
        if (this.liteCommands != null) {
            this.liteCommands.unregister();
        }    
    }

}
