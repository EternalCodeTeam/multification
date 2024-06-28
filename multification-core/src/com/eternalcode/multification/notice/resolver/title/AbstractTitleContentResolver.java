package com.eternalcode.multification.notice.resolver.title;

import com.eternalcode.multification.notice.NoticeKey;
import com.eternalcode.multification.notice.resolver.NoticeSerdesResult;
import com.eternalcode.multification.notice.resolver.text.TextContentResolver;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

public abstract class AbstractTitleContentResolver implements TextContentResolver<TitleContent> {

    private final NoticeKey<TitleContent> key;

    protected AbstractTitleContentResolver(NoticeKey<TitleContent> key) {
        this.key = key;
    }

    @Override
    public NoticeKey<TitleContent> noticeKey() {
        return key;
    }

    @Override
    public TitleContent applyText(TitleContent content, UnaryOperator<String> function) {
        return new TitleContent(function.apply(content.content()));
    }

    @Override
    public NoticeSerdesResult serialize(TitleContent content) {
        return new NoticeSerdesResult.Single(content.content());
    }

    @Override
    public Optional<TitleContent> deserialize(NoticeSerdesResult result) {
        return result.firstElement()
            .map(content -> new TitleContent(content));
    }

    @Override
    public TitleContent createFromText(List<String> contents) {
        if (contents.isEmpty()) {
            return new TitleContent("");
        }

        return new TitleContent(String.join(" ", contents));
    }

}
