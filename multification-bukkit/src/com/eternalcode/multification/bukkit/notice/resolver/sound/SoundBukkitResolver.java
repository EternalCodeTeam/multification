package com.eternalcode.multification.bukkit.notice.resolver.sound;

import com.eternalcode.multification.bukkit.notice.BukkitNoticeKey;
import com.eternalcode.multification.notice.NoticeKey;
import com.eternalcode.multification.notice.resolver.NoticeSerdesResult;
import com.eternalcode.multification.notice.resolver.NoticeResolver;
import java.util.Locale;
import java.util.Optional;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.SoundCategory;

public class SoundBukkitResolver implements NoticeResolver<SoundBukkit> {

    private static final String MUSIC = "%s";
    private static final String MUSIC_WITH_PITCH_VOLUME = "%s %s %s";
    private static final String MUSIC_FULL = "%s %s %s %s";

    @Override
    public NoticeKey<SoundBukkit> noticeKey() {
        return BukkitNoticeKey.SOUND;
    }

    @Override
    public void send(Audience audience, ComponentSerializer<Component, Component, String> componentSerializer, SoundBukkit content) {
        String soundKey = content.sound().getKey().getKey();
        Sound sound = Sound.sound(
            Key.key(soundKey),
            Sound.Source.valueOf(content.toKyoriCategory().name()),
            content.volumeOrDefault(),
            content.pitchOrDefault()
        );

        audience.playSound(sound);
    }

    @Override
    public NoticeSerdesResult serialize(SoundBukkit content) {
        if (content.category() == null) {
            if (content.pitch() == SoundBukkit.PITCH_UNSET || content.volume() == SoundBukkit.VOLUME_UNSET) {
                return new NoticeSerdesResult.Single(String.format(MUSIC, getNameOrLegacyName(content)));
            }

            return new NoticeSerdesResult.Single(String.format(MUSIC_WITH_PITCH_VOLUME,
                getNameOrLegacyName(content),
                content.pitch(),
                content.volume()
            ));
        }

        return new NoticeSerdesResult.Single(String.format(MUSIC_FULL,
            getNameOrLegacyName(content),
            content.category().name(),
            content.pitch(),
            content.volume()
        ));
    }

    /**
     * From 1.21.3, the sound name is returned in "SOME.EPIC.SOUND" format.
     * We want to return it in "some.epic.sound" format, because that's how it's stored in the sound registry.
     * Old versions of Bukkit return the sound name in "SOME_EPIC_SOUND" enum format.
     */
    private static String getNameOrLegacyName(SoundBukkit content) {
        String name = SoundAccessor.name(content.sound());
        if (name.contains(".")) {
            return name.toLowerCase(Locale.ROOT);
        }

        return name;
    }

    @Override
    public Optional<SoundBukkit> deserialize(NoticeSerdesResult result) {
        Optional<String> firstElement = result.firstElement();

        if (firstElement.isEmpty()) {
            return Optional.empty();
        }

        String[] music = firstElement.get().split(" ");

        if (music.length == 1) {
            return Optional.of(new SoundBukkit(SoundAccessor.valueOf(music[0]), null, SoundBukkit.PITCH_UNSET, SoundBukkit.VOLUME_UNSET));
        }

        if (music.length != 4 && music.length != 3) {
            throw new IllegalArgumentException("Invalid sound format: " + firstElement.get());
        }

        org.bukkit.Sound sound = SoundAccessor.valueOf(music[0]);
        SoundCategory category = music.length == 3 ? null : SoundCategory.valueOf(music[1]);
        float pitch = Float.parseFloat(music[music.length - 2]);
        float volume = Float.parseFloat(music[music.length - 1]);

        return Optional.of(new SoundBukkit(sound, category, pitch, volume));
    }

}
