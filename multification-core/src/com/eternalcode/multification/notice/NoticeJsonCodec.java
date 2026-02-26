package com.eternalcode.multification.notice;

import com.eternalcode.multification.notice.resolver.NoticeDeserializeResult;
import com.eternalcode.multification.notice.resolver.NoticeContent;
import com.eternalcode.multification.notice.resolver.NoticeResolverRegistry;
import com.eternalcode.multification.notice.resolver.NoticeSerdesResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

final class NoticeJsonCodec {

    private NoticeJsonCodec() {
    }

    static String serialize(Notice notice, NoticeResolverRegistry noticeRegistry) {
        JsonObject root = new JsonObject();

        for (NoticePart<?> part : notice.parts()) {
            NoticeSerdesResult serializedPart = noticeRegistry.serialize(part);
            root.add(part.noticeKey().key(), toJsonElement(serializedPart));
        }

        return root.toString();
    }

    static Notice deserialize(String raw, NoticeResolverRegistry noticeRegistry) {
        JsonElement parsed = JsonParser.parseString(raw);
        if (!parsed.isJsonObject()) {
            throw new IllegalArgumentException("Notice JSON must be an object");
        }

        Notice.Builder builder = Notice.builder();
        JsonObject root = parsed.getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
            String key = entry.getKey();
            NoticeSerdesResult result = toSerdesResult(key, entry.getValue());
            Optional<NoticeDeserializeResult<?>> deserialized = noticeRegistry.deserialize(key, result);

            if (deserialized.isEmpty()) {
                throw new IllegalArgumentException("Unsupported notice key: " + key);
            }

            withPart(builder, deserialized.get());
        }

        return builder.build();
    }

    private static JsonElement toJsonElement(NoticeSerdesResult result) {
        if (result instanceof NoticeSerdesResult.Single single) {
            return new JsonPrimitive(single.element());
        }

        if (result instanceof NoticeSerdesResult.Multiple multiple) {
            JsonArray array = new JsonArray();
            for (String element : multiple.elements()) {
                array.add(element);
            }
            return array;
        }

        if (result instanceof NoticeSerdesResult.Section section) {
            JsonObject object = new JsonObject();
            for (Map.Entry<String, String> sectionEntry : section.elements().entrySet()) {
                object.addProperty(sectionEntry.getKey(), sectionEntry.getValue());
            }
            return object;
        }

        if (result instanceof NoticeSerdesResult.Empty) {
            return JsonNull.INSTANCE;
        }

        throw new IllegalArgumentException("Unsupported result type: " + result.getClass().getName());
    }

    private static NoticeSerdesResult toSerdesResult(String key, JsonElement element) {
        if (element.isJsonNull()) {
            return new NoticeSerdesResult.Empty();
        }

        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            return new NoticeSerdesResult.Single(element.getAsString());
        }

        if (element.isJsonArray()) {
            return new NoticeSerdesResult.Multiple(toStringList(key, element.getAsJsonArray()));
        }

        if (element.isJsonObject()) {
            return new NoticeSerdesResult.Section(toStringMap(key, element.getAsJsonObject()));
        }

        throw new JsonParseException("Unsupported JSON value for key '" + key + "'");
    }

    private static List<String> toStringList(String key, JsonArray jsonArray) {
        List<String> values = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            if (!jsonElement.isJsonPrimitive() || !jsonElement.getAsJsonPrimitive().isString()) {
                throw new JsonParseException("All array elements for key '" + key + "' must be strings");
            }
            values.add(jsonElement.getAsString());
        }
        return values;
    }

    private static Map<String, String> toStringMap(String key, JsonObject jsonObject) {
        Map<String, String> values = new LinkedHashMap<>();
        for (Map.Entry<String, JsonElement> jsonEntry : jsonObject.entrySet()) {
            JsonElement jsonElement = jsonEntry.getValue();
            if (!jsonElement.isJsonPrimitive() || !jsonElement.getAsJsonPrimitive().isString()) {
                throw new JsonParseException("All object values for key '" + key + "' must be strings");
            }
            values.put(jsonEntry.getKey(), jsonElement.getAsString());
        }
        return values;
    }

    private static <T extends NoticeContent> void withPart(
        Notice.Builder builder,
        NoticeDeserializeResult<T> noticeResult
    ) {
        builder.withPart(noticeResult.noticeKey(), noticeResult.content());
    }

}
