package com.eternalcode.multification.bukkit.notice.resolver.sound;

import com.eternalcode.multification.bukkit.notice.BukkitNoticeKey;
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

    private static final String MUSIC_WITH_CATEGORY = "%s %s %s %s";
    private static final String MUSIC_WITHOUT_CATEGORY = "%s %s %s";

    @Override
    public NoticeKey<SoundBukkit> noticeKey() {
        return BukkitNoticeKey.SOUND;
    }

    @Override
    public void send(Audience audience, ComponentSerializer<Component, Component, String> componentSerializer, SoundBukkit content) {
        String soundKey = content.sound().getKey().getKey();
        Sound sound = content.category() != null
            ? Sound.sound(Key.key(soundKey), Sound.Source.valueOf(content.category().name()), content.volume(), content.pitch())
            : Sound.sound(Key.key(soundKey), Sound.Source.MASTER, content.volume(), content.pitch());

        audience.playSound(sound);
    }

    @Override
    public NoticeSerdesResult serialize(SoundBukkit content) {
        if (content.category() == null) {
            return new NoticeSerdesResult.Single(String.format(MUSIC_WITHOUT_CATEGORY,
                content.sound().name(),
                content.pitch(),
                content.volume()
            ));
        }

        return new NoticeSerdesResult.Single(String.format(MUSIC_WITH_CATEGORY,
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

        if (music.length < 3 || music.length > 4) {
            throw new IllegalStateException("Invalid music format: " + firstElement.get());
        }

        org.bukkit.Sound sound = org.bukkit.Sound.valueOf(music[0]);
        SoundCategory category = music.length == 3 ? null : SoundCategory.valueOf(music[1]);
        float pitch = Float.parseFloat(music[music.length - 2]);
        float volume = Float.parseFloat(music[music.length - 1]);

        return Optional.of(new SoundBukkit(sound, category, pitch, volume));
    }

}
