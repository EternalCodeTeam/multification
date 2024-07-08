package com.eternalcode.multification.cdn;

import com.eternalcode.multification.bukkit.notice.BukkitNotice;
import com.eternalcode.multification.bukkit.notice.BukkitNoticeKey;
import com.eternalcode.multification.bukkit.notice.resolver.sound.SoundBukkit;
import com.eternalcode.multification.bukkit.notice.resolver.sound.SoundBukkitResolver;
import com.eternalcode.multification.notice.Notice;
import com.eternalcode.multification.notice.NoticeKey;
import com.eternalcode.multification.notice.resolver.NoticeResolverDefaults;
import com.eternalcode.multification.notice.resolver.NoticeResolverRegistry;
import com.eternalcode.multification.notice.resolver.actionbar.ActionbarContent;
import com.eternalcode.multification.notice.resolver.chat.ChatContent;
import com.eternalcode.multification.notice.resolver.title.TitleContent;
import com.eternalcode.multification.notice.resolver.title.TitleHide;
import com.eternalcode.multification.notice.resolver.sound.SoundAdventure;
import com.eternalcode.multification.notice.resolver.title.TitleTimes;
import com.eternalcode.multification.notice.NoticePart;
import java.time.Duration;
import net.dzikoysk.cdn.Cdn;
import net.dzikoysk.cdn.CdnFactory;
import net.dzikoysk.cdn.reflect.Visibility;
import net.dzikoysk.cdn.source.Source;

import net.kyori.adventure.key.Key;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("FieldMayBeFinal")
class NoticeComposerTest {

    private static final NoticeResolverRegistry registry = NoticeResolverDefaults.createRegistry()
        .registerResolver(new SoundBukkitResolver());

    private static final Cdn cdn = CdnFactory.createYamlLike()
        .getSettings()
        .withComposer(Notice.class, new MultificationNoticeCdnComposer(registry))
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
        ChatContent chat = assertInstanceOf(ChatContent.class, part.content());
        assertEquals(NoticeKey.CHAT, part.noticeKey());
        assertEquals("Hello world", chat.messages().get(0));
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
        ChatContent chat = assertInstanceOf(ChatContent.class, part.content());
        assertEquals(NoticeKey.CHAT, part.noticeKey());
        assertEquals("First line", chat.messages().get(0));
        assertEquals("Second line", chat.messages().get(1));
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
        TitleContent title = assertInstanceOf(TitleContent.class, part.content());
        assertEquals(NoticeKey.TITLE, part.noticeKey());
        assertEquals("Hello world", title.content());
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
        TitleContent title = assertInstanceOf(TitleContent.class, titlePart.content());
        assertEquals(NoticeKey.TITLE, titlePart.noticeKey());
        assertEquals("Title", title.content());

        NoticePart<?> subtitlePart = configFullTitle.notice.parts().get(1);
        TitleContent subtitle = assertInstanceOf(TitleContent.class, subtitlePart.content());
        assertEquals(NoticeKey.SUBTITLE, subtitlePart.noticeKey());
        assertEquals("Subtitle", subtitle.content());

        NoticePart<?> timesPart = configFullTitle.notice.parts().get(2);
        TitleTimes times = assertInstanceOf(TitleTimes.class, timesPart.content());
        assertEquals(NoticeKey.TITLE_TIMES, timesPart.noticeKey());
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
        ActionbarContent actionbarContent = assertInstanceOf(ActionbarContent.class, part.content());
        assertEquals(NoticeKey.ACTION_BAR, part.noticeKey());
        assertEquals("Hello world", actionbarContent.content());
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
                  hideTitle: true
                """);

        assertEquals(1, configHideTitle.notice.parts().size());

        NoticePart<?> part = configHideTitle.notice.parts().get(0);
        assertInstanceOf(TitleHide.class, part.content());
        assertEquals(NoticeKey.TITLE_HIDE, part.noticeKey());
    }

    static class ConfigSound {
        Notice notice = BukkitNotice.sound(Sound.BLOCK_ANVIL_LAND, SoundCategory.MASTER, 1.0f, 1.0f);
    }
    @Test
    @DisplayName("Should serialize sound notice with sound property")
    void serializeSoundNoticeWithSoundProperty() {
        ConfigSound configSound = assertRender(new ConfigSound(),
            """
                notice:
                  sound: "BLOCK_ANVIL_LAND MASTER 1.0 1.0"
                """);

        assertEquals(1, configSound.notice.parts().size());

        NoticePart<?> part = configSound.notice.parts().get(0);
        SoundBukkit sound = assertInstanceOf(SoundBukkit.class, part.content());
        assertEquals(BukkitNoticeKey.SOUND, part.noticeKey());
        assertEquals(Sound.BLOCK_ANVIL_LAND, sound.sound());
        assertEquals(SoundCategory.MASTER, sound.category());
        assertEquals(1.0f, sound.volume());
        assertEquals(1.0f, sound.pitch());
    }

    static class ConfigSoundWithoutCategory {
        Notice notice = BukkitNotice.sound(Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
    }
    @Test
    @DisplayName("Should serialize sound notice without category property")
    void serializeSoundNoticeWithoutCategoryProperty() {
        ConfigSoundWithoutCategory configSoundWithoutCategory = assertRender(new ConfigSoundWithoutCategory(),
            """
                notice:
                  sound: "BLOCK_ANVIL_LAND 1.0 1.0"
                """);

        assertEquals(1, configSoundWithoutCategory.notice.parts().size());

        NoticePart<?> part = configSoundWithoutCategory.notice.parts().get(0);
        SoundBukkit sound = assertInstanceOf(SoundBukkit.class, part.content());
        assertEquals(BukkitNoticeKey.SOUND, part.noticeKey());
        assertEquals(Sound.BLOCK_ANVIL_LAND, sound.sound());
        assertNull(sound.category());
        assertEquals(1.0f, sound.volume());
        assertEquals(1.0f, sound.pitch());
    }


    static class ConfigSoundShort {
        Notice notice = BukkitNotice.sound(Sound.BLOCK_ANVIL_LAND);
    }
    @Test
    @DisplayName("Should serialize sound notice without volume and pitch")
    void serializeSoundNoticeWithoutVolumeAndPitch() {
        ConfigSoundShort configSoundShort = assertRender(new ConfigSoundShort(),
            """
                notice:
                  sound: "BLOCK_ANVIL_LAND"
                """);

        assertEquals(1, configSoundShort.notice.parts().size());

        NoticePart<?> part = configSoundShort.notice.parts().get(0);
        SoundBukkit sound = assertInstanceOf(SoundBukkit.class, part.content());
        assertEquals(BukkitNoticeKey.SOUND, part.noticeKey());
        assertEquals(Sound.BLOCK_ANVIL_LAND, sound.sound());
        assertNull(sound.category());
        assertEquals(1.0f, sound.volumeOrDefault());
        assertEquals(1.0f, sound.pitchOrDefault());
        assertEquals(-1.0f, sound.volume());
        assertEquals(-1.0f, sound.pitch());
    }


    static class ConfigSoundAdventure {
        Notice notice = Notice.sound(Key.key(Key.MINECRAFT_NAMESPACE, "entity.experience_orb.pickup"), net.kyori.adventure.sound.Sound.Source.MASTER, 1.0f, 1.0f);
    }
    @Test
    @DisplayName("Should serialize adventure sound notice with sound property")
    void serializeSoundNoticeWithSoundAdventureProperty() {
        ConfigSoundAdventure configSound = assertRender(new ConfigSoundAdventure(),
            """
                notice:
                  sound: "entity.experience_orb.pickup MASTER 1.0 1.0"
                """);

        assertEquals(1, configSound.notice.parts().size());

        NoticePart<?> part = configSound.notice.parts().get(0);
        SoundAdventure sound = assertInstanceOf(SoundAdventure.class, part.content());
        assertEquals(NoticeKey.SOUND, part.noticeKey());
        assertEquals("entity.experience_orb.pickup", sound.sound().value());
        assertEquals(net.kyori.adventure.sound.Sound.Source.MASTER, sound.category());
        assertEquals(1.0f, sound.volume());
        assertEquals(1.0f, sound.pitch());
    }

    static class ConfigSoundAdventureWithoutCategory {
        Notice notice = Notice.sound(Key.key(Key.MINECRAFT_NAMESPACE, "entity.experience_orb.pickup"), 1.0f, 1.0f);
    }
    @Test
    @DisplayName("Should serialize adventure sound notice without category property")
    void serializeSoundNoticeWithoutCategoryAdventureProperty() {
        ConfigSoundAdventureWithoutCategory configSound = assertRender(new ConfigSoundAdventureWithoutCategory(),
            """
                notice:
                  sound: "entity.experience_orb.pickup 1.0 1.0"
                """);

        assertEquals(1, configSound.notice.parts().size());

        NoticePart<?> part = configSound.notice.parts().get(0);
        SoundAdventure sound = assertInstanceOf(SoundAdventure.class, part.content());
        assertEquals(NoticeKey.SOUND, part.noticeKey());
        assertEquals("entity.experience_orb.pickup", sound.sound().value());
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
