package com.eternalcode.multification.okaeri;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.eternalcode.multification.notice.NoticeContent.Music;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MultificationMusicTransformerTest {

    private final MultificationMusicTransformer testSubject = new MultificationMusicTransformer();

    @DisplayName("Test Left to Right transformation")
    @ParameterizedTest
    @CsvSource({
        "AMBIENT_CAVE AMBIENT 0.5 1.0, AMBIENT_CAVE, AMBIENT, 0.5, 1.0"
    })
    void shouldTransformLeftToRight(String input, Sound sound, SoundCategory category, float volume, float pitch) {
        Music expectedOutput = new Music(sound, category, volume, pitch);

        Music actualOutput = testSubject.leftToRight(input, null);

        assertEquals(expectedOutput, actualOutput);
    }

    @DisplayName("Test Right to Left transformation")
    @ParameterizedTest
    @CsvSource({
        "AMBIENT_CAVE AMBIENT 0.5 1.0, AMBIENT_CAVE, AMBIENT, 0.5, 1.0"
    })
    void shouldTransformRightToLeft(
        String expectedOutput,
        Sound sound,
        SoundCategory category,
        float volume,
        float pitch
    ) {
        Music input = new Music(sound, category, volume, pitch);

        String actualOutput = testSubject.rightToLeft(input, null);

        assertEquals(expectedOutput, actualOutput);
    }
}