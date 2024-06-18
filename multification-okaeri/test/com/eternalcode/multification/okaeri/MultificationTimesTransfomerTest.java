package com.eternalcode.multification.okaeri;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.eternalcode.multification.notice.NoticeContent.Times;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class MultificationTimesTransformerTest {

    private MultificationTimesTransformer transformer;

    @BeforeEach
    public void setup() {
        transformer = new MultificationTimesTransformer();
    }

    @DisplayName("Test Left to Right transformation")
    @ParameterizedTest
    @CsvSource({
        "'1s 1s 1s', 1, 1, 1",
        "'2s 2s 2s', 2, 2, 2",
        "'3s 3s 3s', 3, 3, 3",
    })
    void testLeftToRight(String strTimes, long fadeIn, long stay, long fadeOut) {
        Times times = transformer.leftToRight(strTimes, null);
        assertEquals(Duration.ofSeconds(fadeIn), times.fadeIn());
        assertEquals(Duration.ofSeconds(stay), times.stay());
        assertEquals(Duration.ofSeconds(fadeOut), times.fadeOut());
    }

    @DisplayName("Test Invalid Left to Right transformation")
    @ParameterizedTest
    @ValueSource(strings = {
        "invalid",
        "notime",
        "1s ts 1s",
        "1s 1s 1s 1s",
        "1s 1s", "1s"
    })
    void testInvalidLeftToRight(String invalid) {
        assertThrows(IllegalArgumentException.class, () -> transformer.leftToRight(invalid, null));
    }

    @DisplayName("Test Right to Left transformation")
    @ParameterizedTest
    @CsvSource({
        "1, 1, 1, '1s 1s 1s'",
        "2, 2, 2, '2s 2s 2s'",
        "3, 3, 3, '3s 3s 3s'"
    })
    void testRightToLeft(long fadeIn, long stay, long fadeOut, String expected) {
        Times times = new Times(Duration.ofSeconds(fadeIn), Duration.ofSeconds(stay), Duration.ofSeconds(fadeOut));
        assertEquals(expected, transformer.rightToLeft(times, null));
    }

    @DisplayName("Test Pair retrieval")
    @Test
    void testGetPair() {
        assertEquals(String.class, transformer.getPair().getFrom().getType());
        assertEquals(Times.class, transformer.getPair().getTo().getType());
    }
}