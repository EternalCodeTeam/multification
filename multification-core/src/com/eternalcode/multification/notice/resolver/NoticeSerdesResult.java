package com.eternalcode.multification.notice.resolver;

import java.util.List;
import java.util.Optional;

public sealed interface NoticeSerdesResult {

    List<String> anyElements();

    default Optional<String> firstElement() {
        return anyElements().stream().findFirst();
    }

    record Empty() implements NoticeSerdesResult {
        @Override
        public List<String> anyElements() {
            return List.of();
        }
    }

    record Single(String element) implements NoticeSerdesResult {
        @Override
        public List<String> anyElements() {
            return List.of(element);
        }
    }

    record Multiple(List<String> elements) implements NoticeSerdesResult {
        @Override
        public List<String> anyElements() {
            return elements;
        }
    }

}
