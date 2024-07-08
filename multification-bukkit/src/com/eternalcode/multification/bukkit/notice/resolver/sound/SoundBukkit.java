package com.eternalcode.multification.bukkit.notice.resolver.sound;

import com.eternalcode.multification.notice.resolver.NoticeContent;
import net.kyori.adventure.sound.Sound.Source;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.jetbrains.annotations.Nullable;

public record SoundBukkit(Sound sound, @Nullable SoundCategory category, float pitch, float volume) implements NoticeContent {

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

    public Source toKyoriCategory() {
        if (category == null) {
            return Source.MASTER;
        }

        return switch (category) {
            case MASTER -> Source.MASTER;
            case MUSIC -> Source.MUSIC;
            case RECORDS -> Source.RECORD;
            case WEATHER -> Source.WEATHER;
            case BLOCKS -> Source.BLOCK;
            case HOSTILE -> Source.HOSTILE;
            case NEUTRAL -> Source.NEUTRAL;
            case PLAYERS -> Source.PLAYER;
            case AMBIENT -> Source.AMBIENT;
            case VOICE -> Source.VOICE;
        };
    }

}
