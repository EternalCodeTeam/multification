package com.eternalcode.multification.cdn;

import com.eternalcode.multification.bukkit.notice.BukkitNotice;
import com.eternalcode.multification.bukkit.notice.resolver.sound.SoundBukkitResolver;
import com.eternalcode.multification.notice.Notice;
import com.eternalcode.multification.notice.NoticeKey;
import com.eternalcode.multification.notice.resolver.NoticeResolverDefaults;
import com.eternalcode.multification.notice.resolver.NoticeResolverRegistry;
import com.eternalcode.multification.notice.resolver.actionbar.ActionbarContent;
import com.eternalcode.multification.notice.resolver.bossbar.BossBarContent;
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

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

@SuppressWarnings("FieldMayBeFinal")
class NoticeComposerTest {

    private static final NoticeResolverRegistry registry = NoticeResolverDefaults.createRegistry()
        .registerResolver(new SoundBukkitResolver());

    private static final Cdn cdn = CdnFactory.createYamlLike()
        .getSettings()
        .withComposer(Notice.class, new MultificationNoticeCdnComposer(registry))
        .withMemberResolver(Visibility.PACKAGE_PRIVATE)
        .build();

    @BeforeAll
    static void setup() {
        MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class);
        bukkit.when(() -> Bukkit.getRegistry(Mockito.any())).thenAnswer(bukkitInvocation -> {
            Class<?> type = bukkitInvocation.getArgument(0);
            Registry<Sound> mock = Mockito.mock(Registry.class);

            if (type == Sound.class) {
                Mockito.when(mock.getOrThrow(Mockito.any())).thenAnswer(registryInvocation -> {
                    NamespacedKey key = registryInvocation.getArgument(0);
                    Sound sound = Mockito.mock(Sound.class);
                    Mockito.when(sound.name())
                        .thenReturn(key.getKey());

                    return sound;
                });
            }

            return mock;
        });
    }

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

    static class ConfigFullTitle {
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
                  sound: "block.anvil.land MASTER 1.0 1.0"
                """);

        assertEquals(1, configSound.notice.parts().size());

        NoticePart<?> part = configSound.notice.parts().get(0);
        SoundAdventure sound = assertInstanceOf(SoundAdventure.class, part.content());
        assertEquals(NoticeKey.SOUND, part.noticeKey());
        assertEquals("block.anvil.land", sound.sound().value());
        assertEquals(net.kyori.adventure.sound.Sound.Source.MASTER, sound.category());
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
                  sound: "block.anvil.land 1.0 1.0"
                """);

        assertEquals(1, configSoundWithoutCategory.notice.parts().size());

        NoticePart<?> part = configSoundWithoutCategory.notice.parts().get(0);
        SoundAdventure sound = assertInstanceOf(SoundAdventure.class, part.content());
        assertEquals(NoticeKey.SOUND, part.noticeKey());
        assertEquals("block.anvil.land", sound.sound().value());
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
                  sound: "block.anvil.land"
                """);

        assertEquals(1, configSoundShort.notice.parts().size());

        NoticePart<?> part = configSoundShort.notice.parts().get(0);
        SoundAdventure sound = assertInstanceOf(SoundAdventure.class, part.content());
        assertEquals(NoticeKey.SOUND, part.noticeKey());
        assertEquals("block.anvil.land", sound.sound().value());
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

    public static class ConfigSoundAdventureShort {
        Notice notice = Notice.sound("ambient.basalt_deltas.additions");
    }

    @Test
    @DisplayName("Should serialize sound notice without volume and pitch")
    void serializeSoundAdventureNoticeWithoutVolumeAndPitch() {
        ConfigSoundAdventureShort config = assertRender(new ConfigSoundAdventureShort(),
            """
                notice:
                  sound: "ambient.basalt_deltas.additions"
                """);

        assertEquals(1, config.notice.parts().size());

        NoticePart<?> part = config.notice.parts().get(0);
        SoundAdventure sound = assertInstanceOf(SoundAdventure.class, part.content());
        assertEquals(NoticeKey.SOUND, part.noticeKey());
        assertEquals("ambient.basalt_deltas.additions", sound.sound().value());
        assertNull(sound.category());
        assertEquals(1.0f, sound.volumeOrDefault());
        assertEquals(1.0f, sound.pitchOrDefault());
        assertEquals(-1.0f, sound.volume());
        assertEquals(-1.0f, sound.pitch());
    }

    static class ConfigBossbarWOBuilder {
        Notice notice = Notice.bossBar(BossBar.Color.PINK, BossBar.Overlay.PROGRESS, Duration.ofSeconds(5), 0.9, "<green>Example boss bar message");
    }

    @Test
    @DisplayName("Should serialize bossbar section without builder with all properties")
    void serializeBossBarSectionWithAllPropertiesWOBuilder() {
        ConfigBossbarWOBuilder configBossBar = assertRender(new ConfigBossbarWOBuilder(),
            """
                notice:
                  bossbar:
                    message: "<green>Example boss bar message"
                    duration: "5s"
                    color: "PINK"
                    overlay: "PROGRESS"
                    progress: 0.9
                """);

        assertEquals(1, configBossBar.notice.parts().size());
        BossBarContent bossBar = assertInstanceOf(BossBarContent.class, configBossBar.notice.parts().get(0).content());
        assertEquals(BossBar.Color.PINK, bossBar.color());
        assertEquals(BossBar.Overlay.PROGRESS, bossBar.overlay().get());
        assertEquals(Duration.ofSeconds(5), bossBar.duration());
        assertThat(bossBar.progress())
            .hasValue(0.9);

        assertEquals("<green>Example boss bar message", bossBar.message());

    }

    static class ConfigBossbarWOProgressWOBuilder {
        Notice notice = Notice.bossBar(BossBar.Color.PINK, BossBar.Overlay.PROGRESS, Duration.ofSeconds(5), "<green>Example boss bar message");
    }

    @Test
    @DisplayName("Should serialize bossbar section without builder and progress")
    void serializeBossBarSectionWithoutProgressWOBuilder() {
        ConfigBossbarWOProgressWOBuilder configBossBar = assertRender(new ConfigBossbarWOProgressWOBuilder(),
            """
                notice:
                  bossbar:
                    message: "<green>Example boss bar message"
                    duration: "5s"
                    color: "PINK"
                    overlay: "PROGRESS"
                """);

        assertEquals(1, configBossBar.notice.parts().size());
        BossBarContent bossBar = assertInstanceOf(BossBarContent.class, configBossBar.notice.parts().get(0).content());
        assertEquals(BossBar.Color.PINK, bossBar.color());
        assertEquals(BossBar.Overlay.PROGRESS, bossBar.overlay().get());
        assertEquals(Duration.ofSeconds(5), bossBar.duration());
        assertThat(bossBar.progress())
            .isEmpty();

        assertEquals("<green>Example boss bar message", bossBar.message());
    }

    static class ConfigBossbarWithoutOverlayWOBuilder {
        Notice notice = Notice.bossBar(BossBar.Color.PINK, Duration.ofSeconds(5), 0.9, "<green>Example boss bar message");
    }

    @Test
    @DisplayName("Should serialize bossbar section without builder and overlay")
    void serializeBossBarSectionWithoutOverlayWOBuilder() {
        ConfigBossbarWithoutOverlayWOBuilder configBossBar = assertRender(new ConfigBossbarWithoutOverlayWOBuilder(),
            """
                notice:
                  bossbar:
                    message: "<green>Example boss bar message"
                    duration: "5s"
                    color: "PINK"
                    progress: 0.9
                """);

        assertEquals(1, configBossBar.notice.parts().size());
        BossBarContent bossBar = assertInstanceOf(BossBarContent.class, configBossBar.notice.parts().get(0).content());
        assertEquals(BossBar.Color.PINK, bossBar.color());

        assertThat(bossBar.overlay())
            .isEmpty();

        assertThat(bossBar.progress())
            .hasValue(0.9);

        assertEquals("<green>Example boss bar message", bossBar.message());
    }

    static class ConfigBossbarWithoutProgressAndOverlayWOBuilder {
        Notice notice = Notice.bossBar(BossBar.Color.PINK, Duration.ofSeconds(5), "<green>Example boss bar message");
    }

    @Test
    @DisplayName("Should serialize bossbar section without builder, progress and overlay")
    void serializeBossBarSectionWithoutProgressAndOverlayWOBuilder() {
        ConfigBossbarWithoutProgressAndOverlayWOBuilder configBossBar = assertRender(new ConfigBossbarWithoutProgressAndOverlayWOBuilder(),
            """
                notice:
                  bossbar:
                    message: "<green>Example boss bar message"
                    duration: "5s"
                    color: "PINK"
                """);

        assertEquals(1, configBossBar.notice.parts().size());
        BossBarContent bossBar = assertInstanceOf(BossBarContent.class, configBossBar.notice.parts().get(0).content());
        assertEquals(BossBar.Color.PINK, bossBar.color());
        assertEquals(Duration.ofSeconds(5), bossBar.duration());

        assertThat(bossBar.overlay())
            .isEmpty();

        assertThat(bossBar.progress())
            .isEmpty();

        assertEquals("<green>Example boss bar message", bossBar.message());
    }

    static class ConfigBossBarWithAllProperties {
        Notice notice = Notice.builder()
            .bossBar(BossBar.Color.PINK, BossBar.Overlay.PROGRESS, Duration.ofSeconds(5), 0.9, "<green>Example boss bar message")
            .build();
    }

    @Test
    @DisplayName("Should serialize bossbar section with all properties")
    void serializeBossBarSectionWithAllProperties() {
        ConfigBossBarWithAllProperties configBossBar = assertRender(new ConfigBossBarWithAllProperties(),
            """
                notice:
                  bossbar:
                    message: "<green>Example boss bar message"
                    duration: "5s"
                    color: "PINK"
                    overlay: "PROGRESS"
                    progress: 0.9
                """);

        assertEquals(1, configBossBar.notice.parts().size());
        BossBarContent bossBar = assertInstanceOf(BossBarContent.class, configBossBar.notice.parts().get(0).content());
        assertEquals(BossBar.Color.PINK, bossBar.color());
        assertEquals(BossBar.Overlay.PROGRESS, bossBar.overlay().get());
        assertEquals(Duration.ofSeconds(5), bossBar.duration());
        assertThat(bossBar.progress())
            .hasValue(0.9);

        assertEquals("<green>Example boss bar message", bossBar.message());

    }

    static class ConfigBossBarWithoutProgress {
        Notice notice = Notice.builder()
            .bossBar(BossBar.Color.PINK, BossBar.Overlay.PROGRESS, Duration.ofSeconds(5), "<green>Example boss bar message")
            .build();
    }

    @Test
    @DisplayName("Should serialize bossbar section without progress")
    void serializeBossBarSectionWithoutProgress() {
        ConfigBossBarWithoutProgress configBossBar = assertRender(new ConfigBossBarWithoutProgress(),
            """
                notice:
                  bossbar:
                    message: "<green>Example boss bar message"
                    duration: "5s"
                    color: "PINK"
                    overlay: "PROGRESS"
                """);

        assertEquals(1, configBossBar.notice.parts().size());
        BossBarContent bossBar = assertInstanceOf(BossBarContent.class, configBossBar.notice.parts().get(0).content());
        assertEquals(BossBar.Color.PINK, bossBar.color());
        assertEquals(BossBar.Overlay.PROGRESS, bossBar.overlay().get());
        assertEquals(Duration.ofSeconds(5), bossBar.duration());
        assertThat(bossBar.progress())
            .isEmpty();

        assertEquals("<green>Example boss bar message", bossBar.message());
    }

    static class ConfigBossBarWithoutOverlay {
        Notice notice = Notice.builder()
            .bossBar(BossBar.Color.PINK, Duration.ofSeconds(5), 0.9, "<green>Example boss bar message")
            .build();
    }

    @Test
    @DisplayName("Should serialize bossbar section without overlay")
    void serializeBossBarSectionWithoutOverlay() {
        ConfigBossBarWithoutOverlay configBossBar = assertRender(new ConfigBossBarWithoutOverlay(),
            """
                notice:
                  bossbar:
                    message: "<green>Example boss bar message"
                    duration: "5s"
                    color: "PINK"
                    progress: 0.9
                """);

        assertEquals(1, configBossBar.notice.parts().size());
        BossBarContent bossBar = assertInstanceOf(BossBarContent.class, configBossBar.notice.parts().get(0).content());
        assertEquals(BossBar.Color.PINK, bossBar.color());

        assertThat(bossBar.overlay())
            .isEmpty();

        assertEquals(Duration.ofSeconds(5), bossBar.duration());
        assertThat(bossBar.progress())
            .hasValue(0.9);

        assertEquals("<green>Example boss bar message", bossBar.message());
    }

    static class ConfigBossBarWithoutProgressAndOverlay {
        Notice notice = Notice.builder()
            .bossBar(BossBar.Color.PINK, Duration.ofSeconds(5), "<green>Example boss bar message")
            .build();
    }

    @Test
    @DisplayName("Should serialize bossbar section without progress and overlay")
    void serializeBossBarSectionWithoutProgressAndOverlay() {
        ConfigBossBarWithoutProgressAndOverlay configBossBar = assertRender(new ConfigBossBarWithoutProgressAndOverlay(),
            """
                notice:
                  bossbar:
                    message: "<green>Example boss bar message"
                    duration: "5s"
                    color: "PINK"
                """);

        assertEquals(1, configBossBar.notice.parts().size());
        BossBarContent bossBar = assertInstanceOf(BossBarContent.class, configBossBar.notice.parts().get(0).content());
        assertEquals(BossBar.Color.PINK, bossBar.color());
        assertEquals(Duration.ofSeconds(5), bossBar.duration());

        assertThat(bossBar.overlay())
            .isEmpty();

        assertThat(bossBar.progress())
            .isEmpty();

        assertEquals("<green>Example boss bar message", bossBar.message());
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
