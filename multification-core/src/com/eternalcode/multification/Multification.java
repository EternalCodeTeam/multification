package com.eternalcode.multification;

import com.eternalcode.multification.locate.LocaleProvider;
import com.eternalcode.multification.translation.TranslationProvider;
import com.eternalcode.multification.viewer.ViewerProvider;
import com.eternalcode.multification.adventure.AudienceConverter;
import com.eternalcode.multification.executor.AsyncExecutor;
import com.eternalcode.multification.notice.NoticeBroadcast;
import com.eternalcode.multification.notice.NoticeBroadcastImpl;
import com.eternalcode.multification.platform.PlatformBroadcaster;
import com.eternalcode.multification.notice.provider.NoticeProvider;
import com.eternalcode.multification.shared.Formatter;
import com.eternalcode.multification.shared.Replacer;
import java.util.Locale;
import java.util.UUID;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public abstract class Multification<VIEWER, TRANSLATION> {

    public static final PlatformBroadcaster DEFAULT_BROADCASTER = PlatformBroadcaster.withPlainSerializer();
    public static final AsyncExecutor DEFAULT_EXECUTOR = runnable -> runnable.run();
    public static final Replacer<?> DEFAULT_REPLACER = (v, text) -> text;
    public static final LocaleProvider<?> LOCALE_PROVIDER = v -> Locale.ROOT;

    @CheckReturnValue
    public NoticeBroadcast<VIEWER, TRANSLATION, ?> create() {
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
    protected abstract ViewerProvider<VIEWER> viewerProvider();

    @NotNull
    protected abstract TranslationProvider<TRANSLATION> translationProvider();

    @NotNull
    protected abstract AudienceConverter<VIEWER> audienceConverter();

    @NotNull
    protected PlatformBroadcaster platformBroadcaster() {
        return DEFAULT_BROADCASTER;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    protected Replacer<VIEWER> globalReplacer() {
        return (Replacer<VIEWER>) DEFAULT_REPLACER;
    }

    @NotNull
    protected AsyncExecutor asyncExecutor() {
        return DEFAULT_EXECUTOR;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    protected LocaleProvider<VIEWER> localeProvider() {
        return (LocaleProvider<VIEWER>) LOCALE_PROVIDER;
    }

    public void player(UUID player, NoticeProvider<TRANSLATION> extractor, Formatter... formatters) {
        this.create()
            .player(player)
            .notice(extractor)
            .formatter(formatters)
            .send();
    }

    public void players(Iterable<UUID> players, NoticeProvider<TRANSLATION> extractor, Formatter... formatters) {
        this.create()
            .players(players)
            .notice(extractor)
            .formatter(formatters)
            .send();
    }

    public void viewer(VIEWER viewer, NoticeProvider<TRANSLATION> extractor, Formatter... formatters) {
        this.create()
            .viewer(viewer)
            .notice(extractor)
            .formatter(formatters)
            .send();
    }


    public void console(NoticeProvider<TRANSLATION> extractor, Formatter... formatters) {
        this.create()
            .console()
            .notice(extractor)
            .formatter(formatters)
            .send();
    }

    public void all(NoticeProvider<TRANSLATION> extractor, Formatter... formatters) {
        this.create()
            .all()
            .notice(extractor)
            .formatter(formatters)
            .send();
    }

}
