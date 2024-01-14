package com.eternalcode.multification.notice;

import com.eternalcode.multification.notice.provider.TextMessageProvider;
import com.eternalcode.multification.shared.Formatter;
import com.eternalcode.multification.notice.provider.NoticeProvider;
import com.eternalcode.multification.notice.provider.OptionalNoticeProvider;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.CheckReturnValue;

@SuppressWarnings("UnstableApiUsage")
public interface NoticeBroadcast<Viewer, Translation, B extends NoticeBroadcast<Viewer, Translation, B>> {

    @CheckReturnValue
    B player(UUID player);

    @CheckReturnValue
    B players(Iterable<UUID> players);

    @CheckReturnValue
    B viewer(Viewer viewer);

    @CheckReturnValue
    B console();

    @CheckReturnValue
    B all();

    @CheckReturnValue
    B onlinePlayers();

    @CheckReturnValue
    B notice(Notice notification);

    @CheckReturnValue
    B notice(NoticeProvider<Translation> extractor);

    @CheckReturnValue
    B notice(NoticeType type, String... text);

    @CheckReturnValue
    B notice(NoticeType type, Collection<String> text);

    @CheckReturnValue
    B notice(NoticeType type, TextMessageProvider<Translation> extractor);

    @CheckReturnValue
    B noticeChat(TextMessageProvider<Translation> extractor);

    @CheckReturnValue
    B noticeChat(Function<Translation, List<String>> function);

    @CheckReturnValue
    B noticeOptional(OptionalNoticeProvider<Translation> extractor);

    @CheckReturnValue
    B placeholder(String from, String to);

    @CheckReturnValue
    B placeholder(String from, Optional<String> to);

    @CheckReturnValue
    B placeholder(String from, Supplier<String> to);

    @CheckReturnValue
    B placeholder(String from, TextMessageProvider<Translation> extractor);

    @CheckReturnValue
    B formatter(Formatter... formatters);

    void sendAsync();

    void send();

}
