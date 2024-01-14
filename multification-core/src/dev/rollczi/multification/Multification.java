package dev.rollczi.multification;

import dev.rollczi.multification.adventure.AudienceConverter;
import dev.rollczi.multification.executor.AsyncExecutor;
import dev.rollczi.multification.locate.LocaleProvider;
import dev.rollczi.multification.notice.NoticeBroadcast;
import dev.rollczi.multification.notice.NoticeBroadcastImpl;
import dev.rollczi.multification.platform.PlatformBroadcaster;
import dev.rollczi.multification.notice.provider.NoticeProvider;
import dev.rollczi.multification.shared.Formatter;
import dev.rollczi.multification.shared.Replacer;
import dev.rollczi.multification.translation.TranslationProvider;
import dev.rollczi.multification.viewer.ViewerProvider;
import java.util.Locale;
import java.util.UUID;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public abstract class Multification<Viewer, Translation> {

    public static final PlatformBroadcaster DEFAULT_BROADCASTER = PlatformBroadcaster.withPlainSerializer();
    public static final AsyncExecutor DEFAULT_EXECUTOR = runnable -> runnable.run();
    public static final Replacer<?> DEFAULT_REPLACER = (v, text) -> text;
    public static final LocaleProvider<?> LOCALE_PROVIDER = v -> Locale.ROOT;

    @CheckReturnValue
    public NoticeBroadcast<Viewer, Translation, ?> create() {
        return new NoticeBroadcastImpl<>(
            this.asyncExecutor(),
            this.translationProvider(),
            this.viewerProvider(),
            this.platformBroadcaster(),
            this.localeProvider(),
            this.audienceConverter(),
            this.globalReplacer()
        );
    }

    @NotNull
    protected abstract ViewerProvider<Viewer> viewerProvider();

    @NotNull
    protected abstract TranslationProvider<Translation> translationProvider();

    @NotNull
    protected abstract AudienceConverter<Viewer> audienceConverter();

    @NotNull
    protected PlatformBroadcaster platformBroadcaster() {
        return DEFAULT_BROADCASTER;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    protected Replacer<Viewer> globalReplacer() {
        return (Replacer<Viewer>) DEFAULT_REPLACER;
    }

    @NotNull
    protected AsyncExecutor asyncExecutor() {
        return DEFAULT_EXECUTOR;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    protected LocaleProvider<Viewer> localeProvider() {
        return (LocaleProvider<Viewer>) LOCALE_PROVIDER;
    }

    public void player(UUID player, NoticeProvider<Translation> extractor, Formatter... formatters) {
        this.create()
            .player(player)
            .notice(extractor)
            .formatter(formatters)
            .send();
    }

    public void players(Iterable<UUID> players, NoticeProvider<Translation> extractor, Formatter... formatters) {
        this.create()
            .players(players)
            .notice(extractor)
            .formatter(formatters)
            .send();
    }

    public void viewer(Viewer viewer, NoticeProvider<Translation> extractor, Formatter... formatters) {
        this.create()
            .viewer(viewer)
            .notice(extractor)
            .formatter(formatters)
            .send();
    }


    public void console(NoticeProvider<Translation> extractor, Formatter... formatters) {
        this.create()
            .console()
            .notice(extractor)
            .formatter(formatters)
            .send();
    }

    public void all(NoticeProvider<Translation> extractor, Formatter... formatters) {
        this.create()
            .all()
            .notice(extractor)
            .formatter(formatters)
            .send();
    }

}
