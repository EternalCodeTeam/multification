package com.eternalcode.multification.notice.resolver.actionbar;

import com.eternalcode.multification.notice.resolver.text.TextContent;
import java.util.List;

public record ActionbarContent(String content) implements TextContent {
    @Override
    public List<String> contents() {
        return List.of(content);
    }
}
