package dev.rollczi.multification.notice;

import java.time.Duration;
import java.util.List;
import net.kyori.adventure.sound.Sound;
import org.jetbrains.annotations.Nullable;

public sealed interface NoticeContent {

    record Text(List<String> messages) implements NoticeContent {
    }

    record Times(Duration fadeIn, Duration stay, Duration fadeOut) implements NoticeContent {
    }

    record Music(Sound.Type sound, @Nullable Sound.Source category, float pitch, float volume) implements NoticeContent {
    }

    record None() implements NoticeContent {

        public static final None INSTANCE = new None();

    }

}
