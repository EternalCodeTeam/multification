package com.eternalcode.multification.notice.resolver.sound;

import com.eternalcode.multification.notice.resolver.NoticeContent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.jetbrains.annotations.Nullable;

public record SoundAdventure(Key sound, @Nullable Sound.Source category, float pitch, float volume) implements NoticeContent {

    public static final Float PITCH_UNSET = -1.F;
    public static final Float VOLUME_UNSET = -1.F;

    public static final float DEFAULT_PITCH = 1.F;
    public static final float DEFAULT_VOLUME = 1.F;

    public float pitchOrDefault() {
        return pitch == PITCH_UNSET ? DEFAULT_PITCH : pitch;
    }

    public float volumeOrDefault() {
        return volume == VOLUME_UNSET ? DEFAULT_VOLUME : volume;
    }

    public Sound.Source categoryOrDefault() {
        return category == null ? Sound.Source.MASTER : category;
    }

}
