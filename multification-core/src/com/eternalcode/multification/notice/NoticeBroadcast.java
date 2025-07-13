package com.eternalcode.multification.notice;

import com.eternalcode.multification.notice.provider.TextMessageProvider;
import com.eternalcode.multification.notice.resolver.text.TextContent;
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

public interface NoticeBroadcast<VIEWER, TRANSLATION, B extends NoticeBroadcast<VIEWER, TRANSLATION, B>> {

    @CheckReturnValue
    B player(UUID player);

    @CheckReturnValue
    B players(Iterable<UUID> players);

    @CheckReturnValue
    B viewer(VIEWER viewer);

    @CheckReturnValue
    B console();

    @CheckReturnValue
    B all();

    @CheckReturnValue
    B onlinePlayers();

    @CheckReturnValue
    B onlinePlayers(String permission);

    @CheckReturnValue
    B notice(Notice notification);

    @CheckReturnValue
    B notice(NoticeProvider<TRANSLATION> extractor);

    @CheckReturnValue
    B notice(NoticeKey<TextContent> type, String... text);

    @CheckReturnValue
    B notice(NoticeKey<TextContent> type, Collection<String> text);

    @CheckReturnValue
    B notice(NoticeKey<TextContent> type, TextMessageProvider<TRANSLATION> extractor);

    @CheckReturnValue
    B noticeChat(TextMessageProvider<TRANSLATION> extractor);

    @CheckReturnValue
    B noticeChat(Function<TRANSLATION, List<String>> function);

    @CheckReturnValue
    B noticeOptional(OptionalNoticeProvider<TRANSLATION> extractor);

    @CheckReturnValue
    B placeholder(String from, String to);

    @CheckReturnValue
    B placeholder(String from, Optional<String> to);

    @CheckReturnValue
    B placeholder(String from, Supplier<String> to);

    @CheckReturnValue
    B placeholder(String from, TextMessageProvider<TRANSLATION> extractor);

    @CheckReturnValue
    B formatter(Formatter... formatters);

    void sendAsync();

    void send();

}
