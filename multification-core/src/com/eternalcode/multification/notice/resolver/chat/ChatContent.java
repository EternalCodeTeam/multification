package com.eternalcode.multification.notice.resolver.chat;

import com.eternalcode.multification.notice.resolver.text.TextContent;
import java.util.List;

public record ChatContent(List<String> messages) implements TextContent {
    @Override
    public List<String> contents() {
        return messages;
    }
}
