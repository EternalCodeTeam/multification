package com.eternalcode.multification.notice;


import com.eternalcode.multification.locate.LocaleProvider;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

class LanguageViewersIndex<VIEWER> {

    private final Map<Locale, Set<VIEWER>> viewersByLanguage;

    private LanguageViewersIndex(Map<Locale, Set<VIEWER>> viewersByLanguage) {
        this.viewersByLanguage = viewersByLanguage;
    }

    Set<Locale> getLocales() {
        return this.viewersByLanguage.keySet();
    }

    Set<VIEWER> getViewers(Locale language) {
        return this.viewersByLanguage.get(language);
    }

    static <V> LanguageViewersIndex<V> of(LocaleProvider<V> localeProvider, Collection<V> viewers) {
        Map<Locale, Set<V>> viewersByLanguage = new HashMap<>();

        for (V viewer : viewers) {
            viewersByLanguage
                .computeIfAbsent(localeProvider.provide(viewer), key -> new HashSet<>())
                .add(viewer);
        }

        return new LanguageViewersIndex<>(viewersByLanguage);
    }

}
