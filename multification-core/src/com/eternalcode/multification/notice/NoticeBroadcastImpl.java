package com.eternalcode.multification.notice;

import com.eternalcode.multification.locate.LocaleProvider;
import com.eternalcode.multification.notice.provider.TextMessageProvider;
import com.eternalcode.multification.shared.Formatter;
import com.eternalcode.multification.translation.TranslationProvider;
import com.eternalcode.multification.viewer.ViewerProvider;
import com.eternalcode.multification.adventure.AudienceConverter;
import com.eternalcode.multification.executor.AsyncExecutor;
import com.eternalcode.multification.notice.provider.NoticeProvider;
import com.eternalcode.multification.notice.provider.OptionalNoticeProvider;
import com.eternalcode.multification.platform.PlatformBroadcaster;
import com.eternalcode.multification.shared.Replacer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.CheckReturnValue;

@SuppressWarnings("UnstableApiUsage")
public class NoticeBroadcastImpl<Viewer, Translation, B extends NoticeBroadcast<Viewer, Translation, B>> implements NoticeBroadcast<Viewer, Translation, B> {

    protected final AsyncExecutor asyncExecutor;
    protected final TranslationProvider<Translation> translationProvider;
    protected final ViewerProvider<Viewer> viewerProvider;
    protected final PlatformBroadcaster platformBroadcaster;
    protected final LocaleProvider<Viewer> localeProvider;
    protected final AudienceConverter<Viewer> audienceConverter;
    protected final Replacer<Viewer> globalReplacer;

    protected final List<Viewer> viewers = new ArrayList<>();
    protected final List<NoticeProvider<Translation>> notifications = new ArrayList<>();

    protected final Map<String, TextMessageProvider<Translation>> placeholders = new HashMap<>();
    protected final List<Formatter> formatters = new ArrayList<>();

    public NoticeBroadcastImpl(
        AsyncExecutor asyncExecutor,
        TranslationProvider<Translation> translationProvider,
        ViewerProvider<Viewer> viewerProvider,
        PlatformBroadcaster platformBroadcaster,
        LocaleProvider<Viewer> localeProvider,
        AudienceConverter<Viewer> audienceConverter, Replacer<Viewer> replacer
    ) {
        this.asyncExecutor = asyncExecutor;
        this.translationProvider = translationProvider;
        this.viewerProvider = viewerProvider;
        this.platformBroadcaster = platformBroadcaster;
        this.localeProvider = localeProvider;
        this.audienceConverter = audienceConverter;
        this.globalReplacer = replacer;
    }

    @Override
    @CheckReturnValue
    public B player(UUID player) {
        this.viewers.add(this.viewerProvider.player(player));
        return this.getThis();
    }

    @Override
    @CheckReturnValue
    public B players(Iterable<UUID> players) {
        Set<Viewer> viewers = new HashSet<>();

        for (UUID player : players) {
            viewers.add(this.viewerProvider.player(player));
        }

        this.viewers.addAll(viewers);
        return this.getThis();
    }

    @Override
    @CheckReturnValue
    public B viewer(Viewer viewer) {
        this.viewers.add(viewer);
        return this.getThis();
    }

    @Override
    @CheckReturnValue
    public B console() {
        this.viewers.add(this.viewerProvider.console());
        return this.getThis();
    }

    @Override
    @CheckReturnValue
    public B all() {
        this.viewers.addAll(this.viewerProvider.all());
        return this.getThis();
    }

    @Override
    @CheckReturnValue
    public B onlinePlayers() {
        this.viewers.addAll(this.viewerProvider.onlinePlayers());
        return this.getThis();
    }

    @Override
    @CheckReturnValue
    public B noticeChat(TextMessageProvider<Translation> extractor) {
        this.notifications.add(translation -> Notice.chat(extractor.extract(translation)));
        return this.getThis();
    }

    @Override
    @CheckReturnValue
    public B noticeChat(Function<Translation, List<String>> function) {
        TextMessageProvider<Translation> translatedMessageExtractor = translation -> {
            List<String> apply = function.apply(translation);

            return String.join("\n", apply);
        };

        return this.noticeChat(translatedMessageExtractor);
    }

    @Override
    @CheckReturnValue
    public B notice(Notice notification) {
        this.notifications.add(translation -> notification);

        return this.getThis();
    }

    @Override
    @CheckReturnValue
    public B noticeOptional(OptionalNoticeProvider<Translation> extractor) {
        this.notifications.add(translation -> {
            Optional<Notice> apply = extractor.extract(translation);

            if (apply.isPresent()) {
                return apply.get();
            }

            return Notice.empty();
        });
        return this.getThis();
    }

    @Override
    @CheckReturnValue
    public B notice(NoticeProvider<Translation> extractor) {
        this.notifications.add(extractor);
        return this.getThis();
    }

    @Override
    public B notice(NoticeType type, String... text) {
        NoticeContent.Text content = new NoticeContent.Text(List.of(text));
        this.notifications.add(translation -> Notice.of(type, content));

        return this.getThis();
    }

    @Override
    public B notice(NoticeType type, Collection<String> text) {
        NoticeContent.Text content = new NoticeContent.Text(new ArrayList<>(text));
        this.notifications.add(translation -> Notice.of(type, content));
        return this.getThis();
    }

    @Override
    public B notice(NoticeType type, TextMessageProvider<Translation> extractor) {
        this.notifications.add(translation -> {
            List<String> list = Collections.singletonList(extractor.extract(translation));
            NoticeContent.Text content = new NoticeContent.Text(list);

            return Notice.of(type, content);
        });

        return this.getThis();
    }

    @Override
    @CheckReturnValue
    public B placeholder(String from, String to) {
        this.placeholders.put(from, translation -> to);
        return this.getThis();
    }

    @Override
    @CheckReturnValue
    public B placeholder(String from, Optional<String> to) {
        if (to.isPresent()) {
            this.placeholders.put(from, translation -> to.get());
        }

        return this.getThis();
    }

    @Override
    @CheckReturnValue
    public B placeholder(String from, Supplier<String> to) {
        this.placeholders.put(from, translation -> to.get());
        return this.getThis();
    }

    @Override
    @CheckReturnValue
    public B placeholder(String from, TextMessageProvider<Translation> extractor) {
        this.placeholders.put(from, extractor);
        return this.getThis();
    }

    @Override
    @CheckReturnValue
    public B formatter(Formatter... formatters) {
        this.formatters.addAll(Arrays.asList(formatters));
        return this.getThis();
    }

    @Override
    public void sendAsync() {
        this.asyncExecutor.execute(() -> send());
    }

    @Override
    public void send() {
        LanguageViewersIndex<Viewer> viewersIndex = LanguageViewersIndex.of(this.localeProvider, this.viewers);
        TranslatedNoticesIndex translatedNoticesIndex = this.prepareTranslatedNotices(viewersIndex.getLocales());

        this.sendTranslatedMessages(viewersIndex, translatedNoticesIndex);
    }

    private void sendTranslatedMessages(LanguageViewersIndex<Viewer> viewersIndex, TranslatedNoticesIndex translatedNoticesIndex) {
        for (Locale language : viewersIndex.getLocales()) {
            List<Notice> notificationsForLang = translatedNoticesIndex.forLanguage(language);

            if (notificationsForLang == null) {
                continue;
            }

            TranslatedFormatter translatedFormatter = this.prepareFormatterForLanguage(language);

            for (Notice notice : notificationsForLang) {
                Set<Viewer> languageViewers = viewersIndex.getViewers(language);

                for (Viewer viewer : languageViewers) {
                    Audience audience = audienceConverter.convert(viewer);

                    for (NoticePart<?> part : notice.parts()) {
                        if (part.type() == NoticeType.SUBTITLE) {
                            List<NoticeType> typesOfNotice = notice.parts().stream().map(noticePart -> noticePart.type()).toList();

                            if (!typesOfNotice.contains(NoticeType.TITLE)) {
                                part = new NoticePart<>(NoticeType.SUBTITLE_WITH_EMPTY_TITLE, part.content());
                            }
                        }

                        part = this.formatter(part, message -> translatedFormatter.format(message, viewer));

                        this.platformBroadcaster.announce(audience, part);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected <T extends NoticeContent> NoticePart<T> formatter(NoticePart<T> part, UnaryOperator<String> function) {
        if (part.content() instanceof NoticeContent.Text text) {
            List<String> messages = text.messages().stream()
                .map(function)
                .toList();

            NoticeContent.Text context = new NoticeContent.Text(messages);

            part = new NoticePart<>(part.type(), (T) context);
        }

        return part;
    }

    private TranslatedNoticesIndex prepareTranslatedNotices(Set<Locale> languages) {
        return TranslatedNoticesIndex.of(languages, language -> {
            Translation translation = this.translationProvider.provide(language);
            List<Notice> notificationsForLanguage = new ArrayList<>();

            for (NoticeProvider<Translation> extractor : this.notifications) {
                notificationsForLanguage.add(extractor.extract(translation));
            }

            return notificationsForLanguage;
        });
    }

    protected TranslatedFormatter prepareFormatterForLanguage(Locale language) {
        Translation translation = this.translationProvider.provide(language);
        Formatter translatedFormatter = new Formatter();

        for (Map.Entry<String, TextMessageProvider<Translation>> entry : this.placeholders.entrySet()) {
            translatedFormatter.register(entry.getKey(), () -> entry.getValue().extract(translation));
        }

        return new TranslatedFormatter(translatedFormatter);
    }

    protected class TranslatedFormatter {

        protected final Formatter translatedPlaceholders;

        protected TranslatedFormatter(Formatter translatedPlaceholders) {
            this.translatedPlaceholders = translatedPlaceholders;
        }

        public String format(String text, Viewer viewer) {
            text = NoticeBroadcastImpl.this.globalReplacer.apply(viewer, text);
            text = this.translatedPlaceholders.format(text);

            for (Formatter formatter : NoticeBroadcastImpl.this.formatters) {
                text = formatter.format(text);
            }

            return text;
        }

    }
    
    @SuppressWarnings("unchecked")
    protected B getThis() {
        return (B) this;
    }

}
