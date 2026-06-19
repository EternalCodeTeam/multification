package com.eternalcode.multification;

import com.eternalcode.multification.locate.LocaleProvider;
import com.eternalcode.multification.translation.TranslationProvider;
import com.eternalcode.multification.viewer.ViewerProvider;
import com.eternalcode.multification.adventure.AudienceConverter;
import com.eternalcode.multification.notice.Notice;
import java.time.Duration;
import com.eternalcode.multification.shared.Replacer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MultificationTest {

    public static final UUID ROLLCZI_UUID = UUID.nameUUIDFromBytes("Rollczi".getBytes());
    public static final UUID LUCKI_UUID = UUID.nameUUIDFromBytes("Lucki".getBytes());

    public static final String JAPAN_MESSAGE = "jp";
    public static final String UK_MESSAGE = "uk";
    public static final String US_MESSAGE = "us";
    public static final String DEFAULT_MESSAGE = "none";
    public static final String OTHER_MESSAGE = "other";

    record Viewer(UUID uuid, Locale locale) {
        static Viewer CONSOLE = new Viewer(UUID.nameUUIDFromBytes(new byte[0]), Locale.ROOT);
    }

    record MyMessages(Notice someMessage) {}

    static class MyViewerProvider implements ViewerProvider<Viewer> {

        static List<Viewer> ONLINE = List.of(
            new Viewer(ROLLCZI_UUID, Locale.JAPAN),
            new Viewer(LUCKI_UUID, Locale.UK)
        );

        @Override
        public Viewer console() {
            return Viewer.CONSOLE;
        }

        @Override
        public Viewer player(UUID uuid) {
            return new Viewer(uuid, Locale.US);
        }

        @Override
        public Collection<Viewer> onlinePlayers() {
            return ONLINE;
        }

        @Override
        public Collection<Viewer> onlinePlayers(String permission) {
            List<Viewer> playersWithPermission = new ArrayList<>();
            for (Viewer viewer : ONLINE) {
                // Simulating permission check
                if (viewer.uuid.equals(ROLLCZI_UUID) || viewer.uuid.equals(LUCKI_UUID)) {
                    playersWithPermission.add(viewer);
                }
            }
            return playersWithPermission;
        }

        @Override
        public Collection<Viewer> all() {
            List<Viewer> list = new ArrayList<>();
            list.add(console());
            list.addAll(onlinePlayers());

            return list;
        }
    }

    static class MyMultification extends Multification<Viewer, MyMessages> {

        private final Map<Locale, MyMessages> myMessagesMap = Map.of(
            Locale.US, new MyMessages(Notice.chat(US_MESSAGE)),
            Locale.UK, new MyMessages(Notice.chat(UK_MESSAGE)),
            Locale.JAPAN, new MyMessages(Notice.chat(JAPAN_MESSAGE))
        );

        @Override
        protected @NotNull ViewerProvider<Viewer> viewerProvider() {
            return new MyViewerProvider();
        }

        @Override
        protected @NotNull TranslationProvider<MyMessages> translationProvider() {
            return locale -> myMessagesMap.getOrDefault(locale, new MyMessages(Notice.chat(DEFAULT_MESSAGE)));
        }

        @Override
        protected @NotNull AudienceConverter<Viewer> audienceConverter() {
            return viewer -> new AudienceMock(viewer.uuid());
        }

        @Override
        protected @NotNull LocaleProvider<Viewer> localeProvider() {
            return viewer -> viewer.locale();
        }

        @Override
        protected @NotNull Replacer<Viewer> globalReplacer() {
            return (viewer, text) -> text.replace("{global}", "-GLOBAL-");
        }
    }

    @Test
    @DisplayName("Should send messages to all viewers")
    void test() {
        MyMultification myMultification = new MyMultification();
        UUID CUSTOM_PLAYER = UUID.randomUUID();

        myMultification.create()
            .onlinePlayers()
            .player(CUSTOM_PLAYER)
            .console()
            .notice(myMessages -> myMessages.someMessage())
            .notice(Notice.chat(OTHER_MESSAGE))
            .notice(Notice.chat("{inner} {global}"))
            .placeholder("{inner}", "-INNER-")
            .send();

        assertThat(AudienceMock.getMessages(LUCKI_UUID))
            .containsExactly(UK_MESSAGE, OTHER_MESSAGE, "-INNER- -GLOBAL-");

        assertThat(AudienceMock.getMessages(ROLLCZI_UUID))
            .containsExactly(JAPAN_MESSAGE, OTHER_MESSAGE, "-INNER- -GLOBAL-");

        assertThat(AudienceMock.getMessages(CUSTOM_PLAYER))
            .containsExactly(US_MESSAGE, OTHER_MESSAGE, "-INNER- -GLOBAL-");

        assertThat(AudienceMock.getMessages(Viewer.CONSOLE.uuid()))
            .containsExactly(DEFAULT_MESSAGE, OTHER_MESSAGE, "-INNER- -GLOBAL-");
    }

    @Test
    @DisplayName("Should serialize and deserialize notice as json")
    void shouldSerializeAndDeserializeNoticeAsJson() {
        MyMultification multification = new MyMultification();
        Notice notice = Notice.builder()
            .chat("line-1", "line-2")
            .actionBar("action")
            .title("title", "subtitle")
            .times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(3))
            .build();

        String raw = multification.serialize(notice);
        Notice restored = multification.deserialize(raw);

        assertThat(raw)
            .contains("\"chat\"")
            .contains("\"actionbar\"")
            .contains("\"times\"");
        assertEquals(notice.parts(), restored.parts());
    }

}
