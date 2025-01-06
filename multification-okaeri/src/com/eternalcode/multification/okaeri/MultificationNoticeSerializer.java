package com.eternalcode.multification.okaeri;

import com.eternalcode.multification.notice.Notice;
import com.eternalcode.multification.notice.Notice.Builder;
import com.eternalcode.multification.notice.NoticeKey;
import com.eternalcode.multification.notice.NoticePart;
import com.eternalcode.multification.notice.resolver.NoticeContent;
import com.eternalcode.multification.notice.resolver.NoticeDeserializeResult;
import com.eternalcode.multification.notice.resolver.NoticeResolverRegistry;
import com.eternalcode.multification.notice.resolver.NoticeSerdesResult;
import com.eternalcode.multification.notice.resolver.NoticeSerdesResult.Multiple;
import com.eternalcode.multification.notice.resolver.NoticeSerdesResult.Single;
import com.eternalcode.multification.notice.resolver.chat.ChatContent;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class MultificationNoticeSerializer implements ObjectSerializer<Notice> {

    private static final int SINGLE_SERIALIZE_DESERIALIZE_PART = 1;

    private final NoticeResolverRegistry noticeRegistry;

    public MultificationNoticeSerializer(NoticeResolverRegistry noticeRegistry) {
        this.noticeRegistry = noticeRegistry;
    }

    @Override
    public boolean supports(@NotNull Class<? super Notice> type) {
        return Notice.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(Notice notice, @NotNull SerializationData data, @NotNull GenericsDeclaration generics) {
        List<NoticePart<?>> parts = notice.parts();

        boolean isChatBeautifulSerialized = trySerializeChatBeautiful(data, notice);

        if (isChatBeautifulSerialized) {
            return;
        }

        for (NoticePart<?> part : parts) {
            NoticeSerdesResult result = this.noticeRegistry.serialize(part);

            if (result instanceof NoticeSerdesResult.Single single) {
                data.add(part.noticeKey().key(), single.element());
                continue;
            }

            if (result instanceof Multiple multiple) {
                data.add(part.noticeKey().key(), multiple.elements());
            }

            if (result instanceof NoticeSerdesResult.Section section) {
                data.add(part.noticeKey().key(), section.elements());
            }
        }
    }

    @Override
    public Notice deserialize(DeserializationData data, @NotNull GenericsDeclaration generics) {
        Builder builder = Notice.builder();

        if (data.isValue()) {
            Object value = data.getValueRaw();

            if (value instanceof String stringValue) {
                List<String> messages = Collections.singletonList(stringValue);
                builder.withPart(NoticeKey.CHAT, new ChatContent(messages));
            }

            if (value instanceof List) {
                List<String> messages = data.getValueAsList(String.class);
                builder.withPart(NoticeKey.CHAT, new ChatContent(messages));
            }

            return builder.build();
        }

        Set<String> keys = data.asMap().keySet();

        for (String key : keys) {
            Object value = data.getRaw(key);

            if (value instanceof String stringValue) {
                NoticeDeserializeResult<?> noticeResult = this.noticeRegistry.deserialize(key, new Single(stringValue))
                    .orElseThrow(() -> new UnsupportedOperationException("Unsupported notice key: " + key + " with value: " + stringValue));

                this.withPart(builder, noticeResult);
                continue;
            }

            if (value instanceof List) {
                List<String> messages = data.getAsList(key, String.class);

                NoticeDeserializeResult<?> noticeResult = this.noticeRegistry.deserialize(key, new Multiple(messages))
                    .orElseThrow(() -> new UnsupportedOperationException("Unsupported notice key: " + key + " with values: " + messages));

                this.withPart(builder, noticeResult);
                continue;
            }

            if (value instanceof Map<?, ?> mapValue) {
                NoticeDeserializeResult<?> noticeResult = this.noticeRegistry.deserialize(key, new NoticeSerdesResult.Section((Map<String, String>) mapValue))
                    .orElseThrow(() -> new UnsupportedOperationException("Unsupported notice key: " + key + " with values: " + mapValue));

                this.withPart(builder, noticeResult);
                continue;
            }

            throw new UnsupportedOperationException("Unsupported notice type: " + value.getClass() + " for key: " + key);
        }

        return builder.build();
    }

    private <T extends NoticeContent> void withPart(Builder builder, NoticeDeserializeResult<T> noticeResult) {
        builder.withPart(noticeResult.noticeKey(), noticeResult.content());
    }

    private static boolean trySerializeChatBeautiful(SerializationData data, Notice notice) {
        List<NoticePart<?>> parts = notice.parts();

        if (parts.size() != 1) {
            return false;
        }

        NoticePart<?> part = parts.get(0);

        if (part.noticeKey() != NoticeKey.CHAT) {
            return false;
        }

        ChatContent chat = (ChatContent) part.content();
        List<String> messages = chat.contents();

        if (messages.size() == SINGLE_SERIALIZE_DESERIALIZE_PART) {
            data.setValue(messages.get(0));
            return true;
        }

        data.setValue(messages);
        return true;
    }
}