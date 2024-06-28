package dev.rollczi.example.bukkit.config;

import com.eternalcode.multification.notice.Notice;
import net.dzikoysk.cdn.entity.Description;

public class MessagesConfig {

    @Description("# Fly command message")
    public Notice flyCommandMessage = Notice.chat("<green>You have toggled fly mode");

}
