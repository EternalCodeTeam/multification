package com.eternalcode.multification.okaeri;

import com.eternalcode.multification.notice.Notice;
import com.eternalcode.multification.notice.Notice.Builder;
import com.eternalcode.multification.notice.NoticeContent.Music;
import com.eternalcode.multification.notice.NoticeContent.None;
import com.eternalcode.multification.notice.NoticeContent.Text;
import com.eternalcode.multification.notice.NoticeContent.Times;
import com.eternalcode.multification.notice.NoticePart;
import com.eternalcode.multification.notice.NoticeType;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;

@Experimental
public class MultificationNoticeSerializer implements ObjectSerializer<Notice> {

    private static final int SINGLE_SERIALIZE_DESERIALIZE_PART = 1;

    private static boolean serializeSingleChat(SerializationData data, NoticePart<?> part) {
        if (part.type() == NoticeType.CHAT) {
            Text text = (Text) part.content();
            List<String> messages = text.messages();

            if (messages.size() == SINGLE_SERIALIZE_DESERIALIZE_PART) {
                data.setValue(messages.get(0));
                return true;
            }

            data.setValue(messages);
            return true;
        }

        return false;
    }

    @Override
    public boolean supports(@NotNull Class<? super Notice> type) {
        return Notice.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(Notice notice, @NotNull SerializationData data, @NotNull GenericsDeclaration generics) {
        List<NoticePart<?>> parts = notice.parts();

        if (parts.size() == SINGLE_SERIALIZE_DESERIALIZE_PART) {
            NoticePart<?> part = parts.get(0);

            if (serializeSingleChat(data, part)) {
                return;
            }
        }

        for (NoticePart<?> part : parts) {
            String key = part.type().getKey();

            if (part.content() instanceof Text text) {
                List<String> messages = text.messages();
                data.add(key, messages);
                continue;
            }

            if (part.content() instanceof Times times) {
                data.add(key, times);
                continue;
            }

            if (part.content() instanceof None none) {
                data.add(key, none);
                continue;
            }

            if (part.content() instanceof Music music) {
                data.add(key, music);
            }
        }
    }

    @Override
    public Notice deserialize(DeserializationData data, @NotNull GenericsDeclaration generics) {

        Builder builder = Notice.builder();

        Set<String> keys = data.asMap().keySet();

        if (keys.size() == SINGLE_SERIALIZE_DESERIALIZE_PART && data.isValue()) {
            Object value = data.getValueRaw();

            if (value instanceof String) {
                List<String> messages = Collections.singletonList((String) value);
                builder.withPart(new NoticePart<>(NoticeType.CHAT, new Text(messages)));
            }

            if (value instanceof List) {
                List<String> messages = data.getValueAsList(String.class);
                builder.withPart(new NoticePart<>(NoticeType.CHAT, new Text(messages)));
            }

            return builder.build();
        }

        for (String key : keys) {
            Object value = data.getRaw(key);

            NoticeType noticeType = NoticeType.fromKey(key);

            if (noticeType.contentType() == Text.class) {
                builder.withPart(new NoticePart<>(noticeType, new Text((List<String>) value)));
                continue;
            }

            if (noticeType.contentType() == Times.class) {
                Times times = data.get(key, Times.class);
                builder.withPart(new NoticePart<>(noticeType, times));
                continue;
            }

            if (noticeType.contentType() == Music.class) {
                Music music = data.get(key, Music.class);
                builder.withPart(new NoticePart<>(noticeType, music));
                continue;
            }

            if (noticeType.contentType() == None.class) {
                builder.withPart(new NoticePart<>(noticeType, None.INSTANCE));
                continue;
            }

            throw new UnsupportedOperationException("Unsupported notice type: " + noticeType);
        }

        return builder.build();
    }
}