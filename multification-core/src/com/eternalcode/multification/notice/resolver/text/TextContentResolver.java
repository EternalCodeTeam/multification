package com.eternalcode.multification.notice.resolver.text;

import com.eternalcode.multification.notice.resolver.NoticeResolver;
import java.util.List;
import java.util.function.UnaryOperator;

public interface TextContentResolver<T extends TextContent> extends NoticeResolver<T> {

    T createFromText(List<String> contents);

    default T applyText(T content, UnaryOperator<String> function) {
        return content;
    }

}
