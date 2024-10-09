package com.eternalcode.multification.okaeri;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.eternalcode.multification.bukkit.notice.BukkitNotice;
import com.eternalcode.multification.bukkit.notice.BukkitNoticeKey;
import com.eternalcode.multification.bukkit.notice.resolver.sound.SoundBukkit;
import com.eternalcode.multification.bukkit.notice.resolver.sound.SoundBukkitResolver;
import com.eternalcode.multification.notice.Notice;
import com.eternalcode.multification.notice.NoticeKey;
import com.eternalcode.multification.notice.NoticePart;
import com.eternalcode.multification.notice.resolver.NoticeResolverDefaults;
import com.eternalcode.multification.notice.resolver.NoticeResolverRegistry;
import com.eternalcode.multification.notice.resolver.actionbar.ActionbarContent;
import com.eternalcode.multification.notice.resolver.bossbar.BossBarContent;
import com.eternalcode.multification.notice.resolver.chat.ChatContent;
import com.eternalcode.multification.notice.resolver.sound.SoundAdventure;
import com.eternalcode.multification.notice.resolver.title.TitleContent;
import com.eternalcode.multification.notice.resolver.title.TitleHide;
import com.eternalcode.multification.notice.resolver.title.TitleTimes;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import java.time.Duration;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("FieldMayBeFinal")
class NoticeSerializerTest {

    private static final NoticeResolverRegistry registry = NoticeResolverDefaults.createRegistry()
        .registerResolver(new SoundBukkitResolver());

    public static class ConfigEmpty extends OkaeriConfig {
        Notice notice = Notice.empty();
    }

    @Test
    @DisplayName("Should serialize and deserialize empty notice to empty entry")
    void serializeEmptyNoticeToEmptyEntry() {
        ConfigEmpty configEmpty = assertRender(new ConfigEmpty(),
            """
                notice: {}
                """
        );

        assertEquals(0, configEmpty.notice.parts().size());
    }

    public static class ConfigOneLineChat extends OkaeriConfig {
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


    public static class ConfigMultiLineChat extends OkaeriConfig {
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

    public static class ConfigSimpleTitle extends OkaeriConfig {
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

    public static class ConfigFullTitle extends OkaeriConfig {
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

    public static class ConfigSimpleActionBar extends OkaeriConfig {
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

    public static class ConfigHideTitle extends OkaeriConfig {
        Notice notice = Notice.hideTitle();
    }

    @Test
    @DisplayName("Should serialize hide title notice with hide title property")
    void serializeHideTitleNoticeWithHideTitleProperty() {
        ConfigHideTitle configHideTitle = assertRender(new ConfigHideTitle(),
            """
                notice:
                  hideTitle: 'true'
                """);

        assertEquals(1, configHideTitle.notice.parts().size());

        NoticePart<?> part = configHideTitle.notice.parts().get(0);
        assertInstanceOf(TitleHide.class, part.content());
        assertEquals(NoticeKey.TITLE_HIDE, part.noticeKey());
    }

    public static class ConfigSound extends OkaeriConfig {
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

    public static class ConfigSoundWithoutCategory extends OkaeriConfig {
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


    public static class ConfigSoundShort extends OkaeriConfig {
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


    public static class ConfigSoundAdventure extends OkaeriConfig {
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

    public static class ConfigSoundAdventureWithoutCategory extends OkaeriConfig {
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

    public static class ConfigBossbarWOBuilder extends OkaeriConfig {
        Notice notice = Notice.bossBar(BossBar.Color.PINK, BossBar.Overlay.PROGRESS, Duration.ofSeconds(5), 0.9, "<green>Example boss bar message");
    }

    @Test
    @DisplayName("Should serialize bossbar w/o builder with all properties")
    void serializeBossBarWOBuilderWithAllProperties() {
        ConfigBossbarWOBuilder configBossBar = assertRender(new ConfigBossbarWOBuilder(),
            """
                notice:
                  bossbar:
                    message: "<green>Example boss bar message"
                    duration: "5s"
                    color: "PINK"
                    overlay: "PROGRESS"
                    progress: '0.9'
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


    public static class ConfigBossbarWithoutProgressWOBuilder extends OkaeriConfig {
        Notice notice = Notice.bossBar(BossBar.Color.PINK, BossBar.Overlay.NOTCHED_6, Duration.ofSeconds(5), "<green>Example boss bar message");
    }

    @Test
    @DisplayName("Should serialize bossbar w/o builder and progress property")
    void serializeBossBarWOBuilderWithoutProgressProperty() {
        ConfigBossbarWithoutProgressWOBuilder configBossBar = assertRender(new ConfigBossbarWithoutProgressWOBuilder(),
            """
                notice:
                  bossbar:
                    message: "<green>Example boss bar message"
                    duration: "5s"
                    color: "PINK"
                    overlay: "NOTCHED_6"
                """);

        assertEquals(1, configBossBar.notice.parts().size());
        BossBarContent bossBar = assertInstanceOf(BossBarContent.class, configBossBar.notice.parts().get(0).content());
        assertEquals(BossBar.Color.PINK, bossBar.color());
        assertEquals(BossBar.Overlay.NOTCHED_6, bossBar.overlay().get());
        assertEquals(Duration.ofSeconds(5), bossBar.duration());
        assertThat(bossBar.progress())
            .isEmpty();

        assertEquals("<green>Example boss bar message", bossBar.message());
    }

    public static class ConfigBossbarWithoutOverlayWOBuilder extends OkaeriConfig {
        Notice notice = Notice.bossBar(BossBar.Color.RED, Duration.ofSeconds(5), 0.9, "<green>Example boss bar message");
    }

    @Test
    @DisplayName("Should serialize bossbar w/o builder and overlay property")
    void serializeBossBarWOBuilderWithoutOverlayProperty() {
        ConfigBossbarWithoutOverlayWOBuilder configBossBar = assertRender(new ConfigBossbarWithoutOverlayWOBuilder(),
            """
                notice:
                  bossbar:
                    message: "<green>Example boss bar message"
                    duration: "5s"
                    color: "RED"
                    progress: '0.9'
                """);

        assertEquals(1, configBossBar.notice.parts().size());
        BossBarContent bossBar = assertInstanceOf(BossBarContent.class, configBossBar.notice.parts().get(0).content());
        assertEquals(BossBar.Color.RED, bossBar.color());

        assertThat(bossBar.overlay())
            .isEmpty();

        assertThat(bossBar.progress())
            .hasValue(0.9);
        assertEquals(Duration.ofSeconds(5), bossBar.duration());

        assertEquals("<green>Example boss bar message", bossBar.message());
    }

    public static class ConfigBossbarWithoutProgressAndOverlayWOBuilder extends OkaeriConfig {
        Notice notice = Notice.bossBar(BossBar.Color.PINK, Duration.ofSeconds(5), "<green>Example boss bar message");
    }

    @Test
    @DisplayName("Should serialize bossbar w/o builder and progress and overlay properties")
    void serializeBossBarWOBuilderWithoutProgressAndOverlayProperties() {
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

        assertThat(bossBar.overlay())
            .isEmpty();

        assertThat(bossBar.progress())
            .isEmpty();
        assertEquals(Duration.ofSeconds(5), bossBar.duration());

        assertEquals("<green>Example boss bar message", bossBar.message());
    }

    static class ConfigBossBar extends OkaeriConfig {
        Notice notice = Notice.builder()
            .bossBar(BossBar.Color.PINK, BossBar.Overlay.PROGRESS, Duration.ofSeconds(5), 0.9, "<green>Example boss bar message")
            .build();
    }

    @Test
    @DisplayName("Should serialize bossbar section with all properties")
    void serializeBossBarSectionWithAllProperties() {
        ConfigBossBar configBossBar = assertRender(new ConfigBossBar(),
            """
                notice:
                  bossbar:
                    message: "<green>Example boss bar message"
                    duration: "5s"
                    color: "PINK"
                    overlay: "PROGRESS"
                    progress: '0.9'
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

    static class ConfigBossBarWithoutProgress extends OkaeriConfig {
        Notice notice = Notice.builder()
            .bossBar(BossBar.Color.PINK, BossBar.Overlay.NOTCHED_6, Duration.ofSeconds(5), "<green>Example boss bar message")
            .build();
    }

    @Test
    @DisplayName("Should serialize bossbar section without progress property")
    void serializeBossBarSectionWithoutProgressProperty() {
        ConfigBossBarWithoutProgress configBossBar = assertRender(new ConfigBossBarWithoutProgress(),
            """
                notice:
                  bossbar:
                    message: "<green>Example boss bar message"
                    duration: "5s"
                    color: "PINK"
                    overlay: "NOTCHED_6"
                """);

        assertEquals(1, configBossBar.notice.parts().size());
        BossBarContent bossBar = assertInstanceOf(BossBarContent.class, configBossBar.notice.parts().get(0).content());
        assertEquals(BossBar.Color.PINK, bossBar.color());
        assertEquals(BossBar.Overlay.NOTCHED_6, bossBar.overlay().get());
        assertEquals(Duration.ofSeconds(5), bossBar.duration());
        assertThat(bossBar.progress())
            .isEmpty();

        assertEquals("<green>Example boss bar message", bossBar.message());
    }

    static class ConfigBossBarWithoutOverlay extends OkaeriConfig {
        Notice notice = Notice.builder()
            .bossBar(BossBar.Color.RED, Duration.ofSeconds(5), 0.9, "<green>Example boss bar message")
            .build();
    }

    @Test
    @DisplayName("Should serialize bossbar section without overlay property")
    void serializeBossBarSectionWithoutOverlayProperty() {
        ConfigBossBarWithoutOverlay configBossBar = assertRender(new ConfigBossBarWithoutOverlay(),
            """
                notice:
                  bossbar:
                    message: "<green>Example boss bar message"
                    duration: "5s"
                    color: "RED"
                    progress: '0.9'
                """);

        assertEquals(1, configBossBar.notice.parts().size());
        BossBarContent bossBar = assertInstanceOf(BossBarContent.class, configBossBar.notice.parts().get(0).content());
        assertEquals(BossBar.Color.RED, bossBar.color());

        assertThat(bossBar.overlay())
            .isEmpty();

        assertEquals(Duration.ofSeconds(5), bossBar.duration());
        assertThat(bossBar.progress())
            .hasValue(0.9);

        assertEquals("<green>Example boss bar message", bossBar.message());
    }

    static class ConfigBossBarWithoutProgressAndOverlay extends OkaeriConfig {
        Notice notice = Notice.builder()
            .bossBar(BossBar.Color.PINK, Duration.ofSeconds(5), "<green>Example boss bar message")
            .build();
    }

    @Test
    @DisplayName("Should serialize bossbar section without progress and overlay properties")
    void serializeBossBarSectionWithoutProgressAndOverlayProperties() {
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

        assertThat(bossBar.overlay())
            .isEmpty();

        assertThat(bossBar.progress())
            .isEmpty();
        assertEquals(Duration.ofSeconds(5), bossBar.duration());

        assertEquals("<green>Example boss bar message", bossBar.message());
    }

    @SuppressWarnings("unchecked")
    private <T extends OkaeriConfig> T assertRender(T entity, String expected) {
        entity.withConfigurer(new YamlSnakeYamlConfigurer(), new MultificationSerdesPack(registry));

        String actual = entity.saveToString();

        actual = removeBlankNewLines(actual);
        expected = removeBlankNewLines(expected);

        assertEquals(expected, actual);

        return (T) entity.load(expected);
    }

    private String removeBlankNewLines(String string) {
        return string
            .replaceAll("\"", "")
            .replaceAll("\n+", "\n")
            .replaceAll("\n+$", "")
            .replaceAll("^\n+", "");
    }

}
