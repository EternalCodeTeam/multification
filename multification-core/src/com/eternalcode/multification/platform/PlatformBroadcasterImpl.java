package com.eternalcode.multification.platform;

import com.eternalcode.multification.notice.NoticeType;
import com.eternalcode.multification.notice.NoticeContent;
import com.eternalcode.multification.notice.NoticePart;
import java.util.Map;
import java.util.function.BiConsumer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.Sound.Source;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;

class PlatformBroadcasterImpl implements PlatformBroadcaster {

    private final Map<NoticeType, NoticePartAnnouncer<?>> announcers = Map.of(
        NoticeType.CHAT,       this.text((audience, message) -> audience.sendMessage(message)),
        NoticeType.ACTION_BAR, this.text((audience, message) -> audience.sendActionBar(message)),
        NoticeType.TITLE,      this.text((audience, title) -> {
            audience.sendTitlePart(TitlePart.TITLE, title);
        }),
        NoticeType.SUBTITLE,   this.text((audience, subtitle) -> {
            audience.sendTitlePart(TitlePart.SUBTITLE, subtitle);
        }),
        NoticeType.SUBTITLE_WITH_EMPTY_TITLE, this.text((audience, subtitle) -> {
            audience.sendTitlePart(TitlePart.TITLE, Component.empty());
            audience.sendTitlePart(TitlePart.SUBTITLE, subtitle);
        }),
        NoticeType.TITLE_TIMES, new TimesNoticePartAnnouncer(),
        NoticeType.TITLE_HIDE, (audience, input) -> audience.clearTitle(),
        NoticeType.SOUND, new SoundNoticePartAnnouncer()
    );

    private final ComponentSerializer<Component, Component, String> componentSerializer;

    public PlatformBroadcasterImpl(ComponentSerializer<Component, Component, String> componentSerializer) {
        this.componentSerializer = componentSerializer;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends NoticeContent> void announce(Audience audience, NoticePart<T> part) {
        NoticePartAnnouncer<T> announcer = (NoticePartAnnouncer<T>) this.announcers.get(part.type());

        if (announcer == null) {
            throw new IllegalStateException("No announcer for " + part.type());
        }

        announcer.announce(audience, part.content());
    }

    interface NoticePartAnnouncer<T extends NoticeContent> {
        void announce(Audience audience, T input);
    }

    private NoticePartAnnouncer<NoticeContent.Text> text(BiConsumer<Audience, Component> consumer) {
        return (audience, input) -> {
            for (String text : input.messages()) {
                consumer.accept(audience, this.componentSerializer.deserialize(text));
            }
        };
    }

    static class TimesNoticePartAnnouncer implements NoticePartAnnouncer<NoticeContent.Times> {
        @Override
        public void announce(Audience audience, NoticeContent.Times timed) {
            Title.Times times = Title.Times.times(
                timed.fadeIn(),
                timed.stay(),
                timed.fadeOut()
            );

            audience.sendTitlePart(TitlePart.TIMES, times);
        }
    }

    static class SoundNoticePartAnnouncer implements NoticePartAnnouncer<NoticeContent.Music> {
        @Override
        public void announce(Audience audience, NoticeContent.Music music) {
            String soundKey = music.sound().getKey().getKey();
            Sound sound = music.category() != null
                ? Sound.sound(Key.key(soundKey), Source.valueOf(music.category().name()), music.volume(), music.pitch())
                : Sound.sound(Key.key(soundKey), Source.MASTER, music.volume(), music.pitch());

            audience.playSound(sound);
        }
    }

}
