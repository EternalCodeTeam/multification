package com.eternalcode.multification.bukkit.notice.resolver.sound;

import com.eternalcode.multification.notice.resolver.NoticeContent;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

public record SoundBukkit(Sound sound, SoundCategory category, float pitch, float volume) implements NoticeContent {


}
