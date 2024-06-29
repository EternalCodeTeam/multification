package com.eternalcode.multification.notice.resolver.sound;

import com.eternalcode.multification.notice.resolver.NoticeContent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.jetbrains.annotations.Nullable;

public record SoundAdventure(Key sound, @Nullable Sound.Source category, float pitch, float volume) implements NoticeContent {
}
