package com.eternalcode.multification.okaeri;

import com.eternalcode.multification.notice.Notice;
import com.eternalcode.multification.notice.NoticePart;
import com.eternalcode.multification.notice.NoticeType;
import com.eternalcode.multification.notice.NoticeContent.*;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.SerializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.schema.GenericsDeclaration;
import java.util.List;

public class MultificationNoticeSerializer implements ObjectSerializer<Notice> {

    @Override
    public boolean supports(Class<? super Notice> type) {
        return Notice.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(Notice notice, SerializationData data, GenericsDeclaration generics) {
        for (NoticePart<?> part : notice.parts()) {
            String key = part.type().getKey();

            if (part.content() instanceof Text text) {
                List<String> messages = text.messages();
                data.add(key, messages);
            }

            if (part.content() instanceof Times times) {
                data.add(key, times);
            }

            if (part.content() instanceof None none) {
                data.add(key, none);
            }

            if (part.content() instanceof Music music) {
                data.add(key, music);
            }
        }
    }

    @Override
    public Notice deserialize(DeserializationData data, GenericsDeclaration generics) {

        Notice.Builder builder = Notice.builder();

        for (NoticeType type : NoticeType.values()) {
            String key = type.getKey();

            if (type.contentType() == Text.class) {
                List<String> messages = data.get(key, List.class);
                builder.withPart(new NoticePart<>(type, new Text(messages)));
                continue;
            }

            if (type.contentType() == Times.class) {
                Times times = data.get(key, Times.class);
                builder.withPart(new NoticePart<>(type, times));
                continue;
            }

            if (type.contentType() == None.class) {
                None none = data.get(key, None.class);
                builder.withPart(new NoticePart<>(type, none));
                continue;
            }

            if (type.contentType() == Music.class) {
                Music music = data.get(key, Music.class);
                builder.withPart(new NoticePart<>(type, music));
            }
        }

        return builder.build();
    }
}