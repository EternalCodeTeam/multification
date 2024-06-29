package com.eternalcode.example.bukkit.notice.resolver.sound;

import com.eternalcode.example.bukkit.notice.BukkitNoticeKey;
import com.eternalcode.multification.notice.NoticeKey;
import com.eternalcode.multification.notice.resolver.NoticeSerdesResult;
import com.eternalcode.multification.notice.resolver.NoticeResolver;
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
                return new NoticeSerdesResult.Single(String.format(MUSIC, content.sound().name()));
            }

            return new NoticeSerdesResult.Single(String.format(MUSIC_WITH_PITCH_VOLUME,
                content.sound().name(),
                content.pitch(),
                content.volume()
            ));
        }

        return new NoticeSerdesResult.Single(String.format(MUSIC_FULL,
            content.sound().name(),
            content.category().name(),
            content.pitch(),
            content.volume()
        ));
    }

    @Override
    public Optional<SoundBukkit> deserialize(NoticeSerdesResult result) {
        Optional<String> firstElement = result.firstElement();

        if (firstElement.isEmpty()) {
            return Optional.empty();
        }

        String[] music = firstElement.get().split(" ");

        if (music.length == 1) {
            return Optional.of(new SoundBukkit(org.bukkit.Sound.valueOf(music[0]), null, SoundBukkit.PITCH_UNSET, SoundBukkit.VOLUME_UNSET));
        }

        if (music.length != 4 && music.length != 3) {
            throw new IllegalArgumentException("Invalid sound format: " + firstElement.get());
        }

        org.bukkit.Sound sound = org.bukkit.Sound.valueOf(music[0]);
        SoundCategory category = music.length == 3 ? null : SoundCategory.valueOf(music[1]);
        float pitch = Float.parseFloat(music[music.length - 2]);
        float volume = Float.parseFloat(music[music.length - 1]);

        return Optional.of(new SoundBukkit(sound, category, pitch, volume));
    }

}
