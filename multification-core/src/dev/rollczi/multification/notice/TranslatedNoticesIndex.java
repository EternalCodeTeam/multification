package dev.rollczi.multification.notice;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class TranslatedNoticesIndex {

    private final Map<Locale, List<Notice>> messages;

    TranslatedNoticesIndex(Map<Locale, List<Notice>> messages) {
        this.messages = messages;
    }

    List<Notice> forLanguage(Locale language) {
        return this.messages.get(language);
    }

    static TranslatedNoticesIndex of(Collection<Locale> languages, Function<Locale, List<Notice>> messages) {
        Map<Locale, List<Notice>> messagesByLanguage = languages.stream()
            .collect(Collectors.toMap(Function.identity(), messages));

        return new TranslatedNoticesIndex(messagesByLanguage);
    }

}
