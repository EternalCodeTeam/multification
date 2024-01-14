package com.eternalcode.multification.cdn;

import com.eternalcode.multification.notice.Notice;
import com.eternalcode.multification.notice.NoticeType;
import static com.eternalcode.multification.notice.NoticeContent.Music;
import static com.eternalcode.multification.notice.NoticeContent.None;
import static com.eternalcode.multification.notice.NoticeContent.Text;
import static com.eternalcode.multification.notice.NoticeContent.Times;
import com.eternalcode.multification.notice.NoticePart;
import java.time.Duration;
import net.dzikoysk.cdn.Cdn;
import net.dzikoysk.cdn.CdnFactory;
import net.dzikoysk.cdn.reflect.Visibility;
import net.dzikoysk.cdn.source.Source;
import net.kyori.adventure.sound.Sound;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("FieldMayBeFinal")
class NoticeComposerTest {

    private static final Cdn cdn = CdnFactory.createYamlLike()
        .getSettings()
        .withComposer(Notice.class, new MultificationNoticeCdnComposer())
        .withMemberResolver(Visibility.PACKAGE_PRIVATE)
        .build();


    static class ConfigEmpty {
        Notice notice = Notice.empty();
    }

    @Test
    @DisplayName("Should serialize and deserialize empty notice to empty entry")
    void serializeEmptyNoticeToEmptyEntry() {
        ConfigEmpty configEmpty = assertRender(new ConfigEmpty(),
            """
                notice: []
                """
        );

        assertEquals(0, configEmpty.notice.parts().size());
    }

    static class ConfigOneLineChat {
        Notice notice = Notice.chat("Hello world");
    }

    @Test
    @DisplayName("Should serialize and deserialize simple chat notice to one line entry")
    void serializeSimpleChatNoticeToOneLineEntry() {
        ConfigOneLineChat oneLineChat = assertRender(new ConfigOneLineChat(),
            """
            notice: "Hello world"
            """
        );

        assertEquals(1, oneLineChat.notice.parts().size());

        NoticePart<?> part = oneLineChat.notice.parts().get(0);
        Text text = assertInstanceOf(Text.class, part.content());
        assertEquals(NoticeType.CHAT, part.type());
        assertEquals("Hello world", text.messages().get(0));
    }


    static class ConfigMultiLineChat {
        Notice notice = Notice.chat("First line", "Second line");
    }
    @Test
    @DisplayName("Should serialize simple chat notice to multiline entry")
    void serializeSimpleChatNoticeToMultilineEntry() {
        ConfigMultiLineChat configMultiLineChat = assertRender(new ConfigMultiLineChat(),
            """
                notice:
                  - "First line"
                  - "Second line"
                """);

        assertEquals(1, configMultiLineChat.notice.parts().size());

        NoticePart<?> part = configMultiLineChat.notice.parts().get(0);
        Text text = assertInstanceOf(Text.class, part.content());
        assertEquals(NoticeType.CHAT, part.type());
        assertEquals("First line", text.messages().get(0));
        assertEquals("Second line", text.messages().get(1));
    }

    static class ConfigSimpleTitle {
        Notice notice = Notice.title("Hello world");
    }
    @Test
    @DisplayName("Should serialize simple title notice to title section")
    void serializeSimpleTitleNoticeToOneLineEntry() {
        ConfigSimpleTitle configSimpleTitle = assertRender(new ConfigSimpleTitle(),
            """
                notice:
                  title: "Hello world"
                """);

        assertEquals(1, configSimpleTitle.notice.parts().size());

        NoticePart<?> part = configSimpleTitle.notice.parts().get(0);
        Text title = assertInstanceOf(Text.class, part.content());
        assertEquals(NoticeType.TITLE, part.type());
        assertEquals("Hello world", title.messages().get(0));
    }

    static  class ConfigFullTitle {
        Notice notice = Notice.title("Title", "Subtitle", Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1));
    }
    @Test
    @DisplayName("Should serialize title subtitle with delay notice to title section")
    void serializeTitleSubtitleWithDelayNoticeToOneLineEntry() {
        ConfigFullTitle configFullTitle = assertRender(new ConfigFullTitle(),
            """
                notice:
                  title: "Title"
                  subtitle: "Subtitle"
                  times: "1s 2s 1s"
                """);

        assertEquals(3, configFullTitle.notice.parts().size());

        NoticePart<?> titlePart = configFullTitle.notice.parts().get(0);
        Text title = assertInstanceOf(Text.class, titlePart.content());
        assertEquals(NoticeType.TITLE, titlePart.type());
        assertEquals("Title", title.messages().get(0));

        NoticePart<?> subtitlePart = configFullTitle.notice.parts().get(1);
        Text subtitle = assertInstanceOf(Text.class, subtitlePart.content());
        assertEquals(NoticeType.SUBTITLE, subtitlePart.type());
        assertEquals("Subtitle", subtitle.messages().get(0));

        NoticePart<?> timesPart = configFullTitle.notice.parts().get(2);
        Times times = assertInstanceOf(Times.class, timesPart.content());
        assertEquals(NoticeType.TITLE_TIMES, timesPart.type());
        assertEquals(1, times.fadeIn().getSeconds());
        assertEquals(2, times.stay().getSeconds());
        assertEquals(1, times.fadeOut().getSeconds());
    }

    static class ConfigSimpleActionBar {
        Notice notice = Notice.actionbar("Hello world");
    }
    @Test
    @DisplayName("Should serialize simple actionbar notice to actionbar section")
    void serializeSimpleActionBarNoticeToOneLineEntry() {
        ConfigSimpleActionBar configSimpleActionBar = assertRender(new ConfigSimpleActionBar(),
            """
                notice:
                  actionbar: "Hello world"
                """);

        assertEquals(1, configSimpleActionBar.notice.parts().size());

        NoticePart<?> part = configSimpleActionBar.notice.parts().get(0);
        Text text = assertInstanceOf(Text.class, part.content());
        assertEquals(NoticeType.ACTION_BAR, part.type());
        assertEquals("Hello world", text.messages().get(0));
    }

    static class ConfigHideTitle {
        Notice notice = Notice.hideTitle();
    }
    @Test
    @DisplayName("Should serialize hide title notice with hide title property")
    void serializeHideTitleNoticeWithHideTitleProperty() {
        ConfigHideTitle configHideTitle = assertRender(new ConfigHideTitle(),
            """
                notice:
                  titleHide: true
                """);

        assertEquals(1, configHideTitle.notice.parts().size());

        NoticePart<?> part = configHideTitle.notice.parts().get(0);
        assertInstanceOf(None.class, part.content());
        assertEquals(NoticeType.TITLE_HIDE, part.type());
    }

    static class ConfigSound {
        Notice notice = Notice.sound("block_anvil_land", Sound.Source.MASTER, 1.0f, 1.0f);
    }
    @Test
    @DisplayName("Should serialize sound notice with sound property")
    void serializeSoundNoticeWithSoundProperty() {
        ConfigSound configSound = assertRender(new ConfigSound(),
            """
                notice:
                  sound: "block_anvil_land MASTER 1.0 1.0"
                """);

        assertEquals(1, configSound.notice.parts().size());

        NoticePart<?> part = configSound.notice.parts().get(0);
        Music sound = assertInstanceOf(Music.class, part.content());
        assertEquals(NoticeType.SOUND, part.type());
        assertEquals("block_anvil_land", sound.sound().key().value());
        assertEquals(Sound.Source.MASTER, sound.category());
        assertEquals(1.0f, sound.volume());
        assertEquals(1.0f, sound.pitch());
    }

    static class ConfigSoundWithoutCategory {
        Notice notice = Notice.sound("block_anvil_land", 1.0f, 1.0f);
    }
    @Test
    @DisplayName("Should serialize sound notice without category property")
    void serializeSoundNoticeWithoutCategoryProperty() {
        ConfigSoundWithoutCategory configSoundWithoutCategory = assertRender(new ConfigSoundWithoutCategory(),
            """
                notice:
                  sound: "block_anvil_land 1.0 1.0"
                """);

        assertEquals(1, configSoundWithoutCategory.notice.parts().size());

        NoticePart<?> part = configSoundWithoutCategory.notice.parts().get(0);
        Music sound = assertInstanceOf(Music.class, part.content());
        assertEquals(NoticeType.SOUND, part.type());
        assertEquals("block_anvil_land", sound.sound().key().value());
        assertNull(sound.category());
        assertEquals(1.0f, sound.volume());
        assertEquals(1.0f, sound.pitch());
    }

    @SuppressWarnings("unchecked")
    private <T> T assertRender(T entity, String expected) {
        String actual = cdn.render(entity).orThrow(exception -> new RuntimeException(exception));

        actual = removeBlankNewLines(actual);
        expected = removeBlankNewLines(expected);

        assertEquals(expected, actual);

        return (T) cdn.load(Source.of(expected), entity.getClass()).orThrow(exception -> new RuntimeException(exception));
    }

    private String removeBlankNewLines(String string) {
        return string
            .replaceAll("\n+", "\n")
            .replaceAll("\n+$", "")
            .replaceAll("^\n+", "");
    }

}
