package com.eternalcode.multification.notice.resolver;

import com.eternalcode.multification.notice.NoticeKey;
import com.eternalcode.multification.notice.NoticePart;
import com.eternalcode.multification.notice.resolver.text.TextContent;
import com.eternalcode.multification.notice.resolver.text.TextContentResolver;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.jetbrains.annotations.Nullable;

public class NoticeResolverRegistry {

    private final Map<NoticeKey<?>, NoticeResolver<?>> resolvers = new HashMap<>();
    private final Map<String, Set<NoticeResolver<?>>> resolversByKey = new HashMap<>();

    public <T extends NoticeContent> NoticeResolverRegistry registerResolver(NoticeResolver<T> resolver) {
        resolvers.put(resolver.noticeKey(), resolver);
        resolversByKey.computeIfAbsent(resolver.noticeKey().key(), key -> new HashSet<>()).add(resolver);
        return this;
    }

    public <T extends NoticeContent> NoticeSerdesResult serialize(NoticePart<T> noticePart) {
        NoticeResolver<T> resolver = this.getNoticeResolverOrThrow(noticePart.noticeKey());

        return resolver.serialize(noticePart.content());
    }

    public Optional<NoticeDeserializeResult<?>> deserialize(String key, NoticeSerdesResult result) {
        Set<NoticeResolver<?>> resolvers = this.resolversByKey.get(key);

        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Could not deserialize " + result);

        for (NoticeResolver<?> resolver : resolvers) {
            try {
                NoticeDeserializeResult<?> deserializeResult = this.deserialize(resolver, result);

                if (deserializeResult != null) {
                    return Optional.of(deserializeResult);
                }
            }
            catch (Exception exception) {
                illegalArgumentException.addSuppressed(exception);
            }
        }

        if (illegalArgumentException.getSuppressed().length > 0) {
            throw illegalArgumentException;
        }

        return Optional.empty();
    }

    @Nullable
    private <T extends NoticeContent> NoticeDeserializeResult<T> deserialize(NoticeResolver<T> resolver, NoticeSerdesResult result) {
        return resolver.deserialize(result)
            .map(content -> new NoticeDeserializeResult<>(resolver.noticeKey(), content))
            .orElse(null);
    }

    public <T extends NoticeContent> void send(Audience audience, ComponentSerializer<Component, Component, String> serializer, NoticePart<T> noticePart) {
        NoticeResolver<T> resolver = this.getNoticeResolverOrThrow(noticePart.noticeKey());

        resolver.send(audience, serializer, noticePart.content());
    }

    @SuppressWarnings("unchecked")
    private <T extends NoticeContent> NoticeResolver<T> getNoticeResolverOrThrow(NoticeKey<T> key) {
        NoticeResolver<T> resolver = (NoticeResolver<T>) resolvers.get(key);

        if (resolver == null) {
            throw new IllegalArgumentException("No resolver found for " + key + " - " + key);
        }

        return resolver;
    }

    public <T extends TextContent> T createTextNotice(NoticeKey<T> noticeKey, List<String> contents) {
        NoticeResolver<T> resolver = this.getNoticeResolverOrThrow(noticeKey);

        if (!(resolver instanceof TextContentResolver<T> textContentResolver)) {
            throw new IllegalArgumentException("Resolver for " + noticeKey + " is not a text resolver");
        }

        return textContentResolver.createFromText(contents);
    }

    public <T extends TextContent> NoticePart<T> applyText(NoticePart<T> part, UnaryOperator<String> function) {
        NoticeResolver<T> resolver = this.getNoticeResolverOrThrow(part.noticeKey());

        if (resolver instanceof TextContentResolver<T> textContentResolver) {
            T t = textContentResolver.applyText(part.content(), function);

            return new NoticePart<>(part.noticeKey(), t);
        }

        return part;
    }

}
