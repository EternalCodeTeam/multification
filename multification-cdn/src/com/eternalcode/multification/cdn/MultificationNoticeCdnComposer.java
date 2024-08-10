package com.eternalcode.multification.cdn;

import com.eternalcode.multification.Multification;
import com.eternalcode.multification.notice.Notice;
import com.eternalcode.multification.notice.resolver.NoticeDeserializeResult;
import com.eternalcode.multification.notice.resolver.NoticeContent;
import com.eternalcode.multification.notice.NoticeKey;
import com.eternalcode.multification.notice.resolver.NoticeResolverRegistry;
import com.eternalcode.multification.notice.resolver.NoticeSerdesResult;
import com.eternalcode.multification.notice.resolver.NoticeSerdesResult.Multiple;
import com.eternalcode.multification.notice.resolver.chat.ChatContent;

import com.eternalcode.multification.notice.NoticePart;
import java.util.List;
import java.util.Optional;
import net.dzikoysk.cdn.CdnSettings;
import net.dzikoysk.cdn.CdnUtils;
import net.dzikoysk.cdn.model.Element;
import net.dzikoysk.cdn.model.Entry;
import net.dzikoysk.cdn.model.Piece;
import net.dzikoysk.cdn.model.Section;
import net.dzikoysk.cdn.module.standard.StandardOperators;
import net.dzikoysk.cdn.reflect.TargetType;
import net.dzikoysk.cdn.serdes.Composer;
import panda.std.Result;

public class MultificationNoticeCdnComposer implements Composer<Notice> {

    private static final String EMPTY_NOTICE = "[]";

    private final NoticeResolverRegistry noticeRegistry;

    public MultificationNoticeCdnComposer(Multification<?, ?> multification) {
        this.noticeRegistry = multification.getNoticeRegistry();
    }

    public MultificationNoticeCdnComposer(NoticeResolverRegistry noticeRegistry) {
        this.noticeRegistry = noticeRegistry;
    }

    @Override
    public Resul<? extends Element<?>, ? extends Exception> serialize(CdnSettings settings, List<String> description, String key, TargetType type, Notice entity) {
        SerializeContext context = new SerializeContext(settings, description, key, type, entity);

        return this.serializeEmpty(context)
            .orElse(error -> this.serializerUndisclosedChat(context))
            .orElse(error -> this.serializeAll(context));
    }

    private Result<Element<?>, Exception> serializeEmpty(SerializeContext context) {
        if (context.notice.parts().isEmpty()) {
            return Result.ok(empty(context));
        }

        return Result.error(new IllegalStateException("Notice is not empty"));
    }

    private Result<Element<?>, Exception> serializerUndisclosedChat(SerializeContext context) {
        List<NoticePart<?>> parts = context.notice.parts();

        if (parts.size() != 1) {
            return Result.error(new IllegalStateException("Notice is not one part"));
        }

        NoticePart<?> part = parts.get(0);

        if (!NoticeKey.CHAT.equals(part.noticeKey())) {
            return Result.error(new IllegalStateException("Notice is not chat"));
        }

        if (!(part.content() instanceof ChatContent chat)) {
            return Result.error(new IllegalStateException("Notice is not text"));
        }

        List<String> messages = chat.messages();

        if (messages.isEmpty()) {
            return Result.ok(empty(context));
        }

        if (messages.size() == 1) {
            return Result.ok(oneLine(context.key, context.description, messages.get(0)));
        }

        return Result.ok(toSection(context.key, context.description, messages));
    }

    private Result<Element<?>, Exception> serializeAll(SerializeContext context) {
        List<NoticePart<?>> parts = context.notice.parts();
        Section section = new Section(context.description, context.key);

        for (NoticePart<?> part : parts) {
            String key = part.noticeKey().key();
            NoticeSerdesResult result = noticeRegistry.serialize(part);

            if (result instanceof NoticeSerdesResult.Single single) {
                section.append(new Entry(List.of(), key, single.element()));
                continue;
            }

            if (result instanceof Multiple multiple) {
                section.append(toSection(key, List.of(), multiple.elements()));
                continue;
            }

            return Result.error(new UnsupportedOperationException("Unsupported notice type: " + part.noticeKey() + ": " + part.content()));
        }

        return Result.ok(section);
    }

    private static Element<?> empty(SerializeContext context) {
        return oneLine(context.key, context.description, EMPTY_NOTICE);
    }

    private static Element<?> oneLine(String key, List<String> description, String value) {
        return key == null || key.isEmpty() ? new Piece(value) : new Entry(description, key, value);
    }

    private static Section toSection(String key, List<String> description, List<String> elements) {
        Section section = new Section(description, key);

        for (String message : elements) {
            section.append(new Piece(StandardOperators.ARRAY + " " + CdnUtils.stringify(true, message)));
        }

        return section;
    }

    private record SerializeContext(CdnSettings settings, List<String> description, String key, TargetType type,
                                    Notice notice) {
    }

    @Override
    public Result<Notice, Exception> deserialize(CdnSettings settings, Element<?> source, TargetType type, Notice defaultValue, boolean entryAsRecord) {
        DeserializeContext context = new DeserializeContext(settings, source, type, defaultValue, entryAsRecord);

        return this.deserializeEmpty(context)
            .orElse(error -> this.deserializeAll(context));
    }

    private Result<Notice, Exception> deserializeEmpty(DeserializeContext context) {
        if (context.source() instanceof Piece piece && piece.getValue().equals(EMPTY_NOTICE)) {
            return Result.ok(Notice.empty());
        }

        if (context.source() instanceof Entry entry && entry.getPieceValue().equals(EMPTY_NOTICE)) {
            return Result.ok(Notice.empty());
        }

        return Result.error(new IllegalStateException("Notice is not empty"));
    }

    private Result<Notice, Exception> deserializeAll(DeserializeContext context) {
        // - "Hello, world!"
        if (context.source() instanceof Piece piece) {
            return Result.ok(Notice.chat(CdnUtils.destringify(piece.getValue())));
        }

        // example: "Hello, world!"
        if (context.source() instanceof Entry entry) {
            return Result.ok(Notice.chat(CdnUtils.destringify(entry.getPieceValue())));
        }

        /*
        example:
           - "Hello, world!"
           action-bar: "Hello, world!"
         */
        if (context.source() instanceof Section section) {
            return this.deserializeSection(section);
        }

        return Result.error(new UnsupportedOperationException("Unsupported element type: " + context.source().getClass()));
    }

    private Result<Notice, Exception> deserializeSection(Section section) {
        Notice.Builder builder = Notice.builder();

        for (Element<?> element : section.getValue()) {
            // - "Hello, world!"
            if (element instanceof Piece piece) {
                builder.chat(this.deserializePiece(piece));
                continue;
            }

            // actionbar: "Hello, world!"
            if (element instanceof Entry entry) {
                String value = this.deserializePiece(entry.getValue());
                String key = entry.getName();

                NoticeSerdesResult.Single result = new NoticeSerdesResult.Single(value);
                Optional<NoticeDeserializeResult<?>> optional = this.noticeRegistry.deserialize(key, result);

                if (optional.isEmpty()) {
                    return Result.error(new IllegalStateException("Unsupported notice type: " + key + ": " + value));
                }

                this.withPart(builder, optional.get());
                continue;
            }

            if (element instanceof Section subSection) {
                for (Element<?> subElement : subSection.getValue()) {
                    if (!(subElement instanceof Piece piece)) {
                        throw new IllegalStateException("Unsupported element type: " + subElement.getValue());
                    }

                    builder.chat(this.deserializePiece(piece));
                }

                continue;
            }

            return Result.error(new UnsupportedOperationException("Unsupported element type: " + element.getClass()));
        }

        return Result.ok(builder.build());
    }

    private <T extends NoticeContent> void withPart(Notice.Builder builder, NoticeDeserializeResult<T> result) {
        builder.withPart(result.noticeKey(), result.content());
    }

    private String deserializePiece(Piece piece) {
        String value = piece.getValue();

        if (value.startsWith(StandardOperators.ARRAY)) {
            value = value.substring(StandardOperators.ARRAY.length());
        }

        return CdnUtils.destringify(value.trim());
    }

    record DeserializeContext(CdnSettings settings, Element<?> source, TargetType type, Notice defaultValue, boolean entryAsRecord) {
    }

}
