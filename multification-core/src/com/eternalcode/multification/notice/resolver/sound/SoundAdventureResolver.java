package com.eternalcode.multification.notice.resolver.sound;

import com.eternalcode.multification.notice.NoticeKey;
import com.eternalcode.multification.notice.resolver.NoticeResolver;
import com.eternalcode.multification.notice.resolver.NoticeSerdesResult;
import java.util.Optional;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

public class SoundAdventureResolver implements NoticeResolver<SoundAdventure> {

    private static final String MUSIC_WITH_CATEGORY = "%s %s %s %s";
    private static final String MUSIC_WITHOUT_CATEGORY = "%s %s %s";

    private final NoticeKey<SoundAdventure> key;

    public SoundAdventureResolver() {
        this.key = NoticeKey.SOUND;
    }

    @Override
    public NoticeKey<SoundAdventure> noticeKey() {
        return key;
    }

    @Override
    public void send(Audience audience, ComponentSerializer<Component, Component, String> componentSerializer, SoundAdventure content) {
        if (content.category() == null) {
            audience.playSound(Sound.sound(content.sound(), Sound.Source.MASTER, content.volume(), content.pitch()));
            return;
        }

        audience.playSound(Sound.sound(content.sound(), content.category(), content.volume(), content.pitch()));
    }

    @Override
    public NoticeSerdesResult serialize(SoundAdventure content) {
        if (content.category() == null) {
            return new NoticeSerdesResult.Single(String.format(MUSIC_WITHOUT_CATEGORY,
                content.sound().value(),
                content.pitch(),
                content.volume()
            ));
        }

        return new NoticeSerdesResult.Single(String.format(MUSIC_WITH_CATEGORY,
            content.sound().value(),
            content.category().name(),
            content.pitch(),
            content.volume()
        ));
    }

    @Override
    public Optional<SoundAdventure> deserialize(NoticeSerdesResult result) {
        Optional<String> firstElement = result.firstElement();

        if (firstElement.isEmpty()) {
            return Optional.empty();
        }

        String[] music = firstElement.get().split(" ");

        if (music.length < 3 || music.length > 4) {
            throw new IllegalStateException("Invalid music format: " + firstElement.get());
        }

        Key sound = Key.key(Key.MINECRAFT_NAMESPACE, music[0]);
        Sound.Source category = music.length == 3 ? null : Sound.Source.valueOf(music[1]);
        float pitch = Float.parseFloat(music[music.length - 2]);
        float volume = Float.parseFloat(music[music.length - 1]);

        return Optional.of(new SoundAdventure(sound, category, pitch, volume));
    }

}
