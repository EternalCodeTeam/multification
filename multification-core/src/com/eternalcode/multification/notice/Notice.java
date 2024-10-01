package com.eternalcode.multification.notice;

import com.eternalcode.multification.notice.resolver.NoticeContent;
import com.eternalcode.multification.notice.resolver.actionbar.ActionbarContent;
import com.eternalcode.multification.notice.resolver.bossbar.BossBarContent;
import com.eternalcode.multification.notice.resolver.chat.ChatContent;
import com.eternalcode.multification.notice.resolver.title.TitleContent;
import com.eternalcode.multification.notice.resolver.title.TitleHide;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;

import com.eternalcode.multification.notice.resolver.sound.SoundAdventure;
import com.eternalcode.multification.notice.resolver.title.TitleTimes;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;

public class Notice {

    private final Map<NoticeKey<?>, NoticePart<?>> parts = new LinkedHashMap<>();

    protected Notice(Map<NoticeKey<?>, NoticePart<?>> parts) {
        this.parts.putAll(parts);
    }

    public List<NoticePart<?>> parts() {
        return this.parts.values().stream()
            .toList();
    }

    public static <T extends NoticeContent> Notice of(NoticeKey<T> key, T content) {
        return Notice.builder()
            .withPart(key, content)
            .build();
    }

    public static Notice chat(String... messages) {
        return Notice.builder()
            .chat(messages)
            .build();
    }

    public static Notice chat(Collection<String> messages) {
        return Notice.builder()
            .chat(messages)
            .build();
    }

    public static Notice actionbar(String actionBar) {
        return Notice.builder()
            .actionBar(actionBar)
            .build();
    }

    public static Notice title(String title) {
        return Notice.builder()
            .title(title)
            .build();
    }

    public static Notice subtitle(String subtitle) {
        return Notice.builder()
            .subtitle(subtitle)
            .build();
    }

    public static Notice title(String title, String subtitle) {
        return Notice.builder()
            .title(title)
            .subtitle(subtitle)
            .build();
    }

    public static Notice title(String title, String subtitle, Duration fadeIn, Duration stay, Duration fadeOut) {
        return Notice.builder()
            .title(title)
            .subtitle(subtitle)
            .times(fadeIn, stay, fadeOut)
            .build();
    }

    public static Notice hideTitle() {
        return Notice.builder()
            .hideTitle()
            .build();
    }

    public static Notice sound(Key sound, Sound.Source category, float volume, float pitch) {
        return Notice.builder()
            .sound(sound, category, pitch, volume)
            .build();
    }

    public static Notice sound(Key sound, float volume, float pitch) {
        return Notice.builder()
            .sound(sound, pitch, volume)
            .build();
    }

    public static Notice bossBar(BossBar.Color color, BossBar.Overlay overlay, Duration duration, double progress, String message) {
        return Notice.builder()
            .bossBar(color, overlay, duration, progress, message)
            .build();
    }

    public static Notice bossBar(BossBar.Color color, Duration duration, double progress, String message) {
        return Notice.builder()
            .bossBar(color, duration, progress, message)
            .build();
    }

    public static Notice bossBar(BossBar.Color color, BossBar.Overlay overlay, Duration duration, String message) {
        return Notice.builder()
            .bossBar(color, overlay, duration, message)
            .build();
    }

    public static Notice bossBar(BossBar.Color color, Duration duration, String message) {
        return Notice.builder()
            .bossBar(color, duration, message)
            .build();
    }

    public static Notice empty() {
        return new Notice(Collections.emptyMap());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseBuilder<Builder> {
        @Override
        protected Builder getThis() {
            return this;
        }
    }

    public abstract static class BaseBuilder<B extends BaseBuilder<B>> {
        protected final Map<NoticeKey<?>, NoticePart<?>> parts = new LinkedHashMap<>();

        public B withPart(NoticePart<?> part) {
            this.parts.put(part.noticeKey(), part);
            return this.getThis();
        }

        public <T extends NoticeContent> B withPart(NoticeKey<T> key, T content) {
            return this.withPart(new NoticePart<>(key, content));
        }

        abstract protected B getThis();

        public Notice build() {
            return new Notice(this.parts);
        }

        public B chat(String... messages) {
            return this.chat(List.of(messages));
        }

        public B chat(Collection<String> messages) {
            NoticePart<?> removed = this.parts.remove(NoticeKey.CHAT);
            List<String> newMessages = new ArrayList<>();

            if (removed != null && removed.content() instanceof ChatContent chat) {
                newMessages.addAll(chat.messages());
            }

            newMessages.addAll(messages);

            return this.withPart(NoticeKey.CHAT, new ChatContent(Collections.unmodifiableList(newMessages)));
        }

        public B actionBar(String message) {
            return this.withPart(NoticeKey.ACTION_BAR, new ActionbarContent(message));
        }

        public B title(String title) {
            return this.withPart(NoticeKey.TITLE, new TitleContent(title));
        }

        public B title(String title, String subtitle) {
            return this.withPart(NoticeKey.TITLE, new TitleContent(title))
                .withPart(NoticeKey.SUBTITLE, new TitleContent(subtitle));
        }

        public B subtitle(String subtitle) {
            return this.withPart(NoticeKey.SUBTITLE, new TitleContent(subtitle));
        }

        public B hideTitle() {
            return this.withPart(NoticeKey.TITLE_HIDE, new TitleHide(true));
        }

        public B hideTitle(boolean hide) {
            return this.withPart(NoticeKey.TITLE_HIDE, new TitleHide(hide));
        }

        public B times(Duration in, Duration stay, Duration out) {
            return this.withPart(NoticeKey.TITLE_TIMES, new TitleTimes(in, stay, out));
        }

        public B sound(Key sound, float pitch, float volume) {
            return this.withPart(NoticeKey.SOUND, new SoundAdventure(sound, null, pitch, volume));
        }

        public B sound(Key sound, Sound.Source category, float pitch, float volume) {
            return this.withPart(NoticeKey.SOUND, new SoundAdventure(sound, category, pitch, volume));
        }

        public B bossBar(BossBar.Color color, BossBar.Overlay overlay, Duration duration, double progress, String message) {
            return this.withPart(NoticeKey.BOSS_BAR, new BossBarContent(color, Optional.of(overlay), duration, OptionalDouble.of(progress), message));
        }

        public B bossBar(BossBar.Color color, Duration duration, double progress, String message) {
            return this.withPart(NoticeKey.BOSS_BAR, new BossBarContent(color, Optional.empty(), duration, OptionalDouble.of(progress), message));
        }

        public B bossBar(BossBar.Color color, BossBar.Overlay overlay, Duration duration, String message) {
            return this.withPart(NoticeKey.BOSS_BAR, new BossBarContent(color, Optional.of(overlay), duration, OptionalDouble.empty(), message));
        }

        public B bossBar(BossBar.Color color, Duration duration, String message) {
            return this.withPart(NoticeKey.BOSS_BAR, new BossBarContent(color, Optional.empty(), duration, OptionalDouble.empty(), message));
        }

        public B bossBar(BossBar.Color color, BossBar.Overlay overlay, Duration duration, String message) {
            return this.withPart(NoticeKey.BOSS_BAR, new BossBarContent(color, overlay, duration, OptionalDouble.empty(), message));
        }

    }

}
