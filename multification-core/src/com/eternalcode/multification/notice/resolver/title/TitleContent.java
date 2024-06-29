package com.eternalcode.multification.notice.resolver.title;

import com.eternalcode.multification.notice.resolver.text.TextContent;
import java.util.List;

public record TitleContent(String content) implements TextContent {
    @Override
    public List<String> contents() {
        return List.of(content);
    }
}
