package dev.rollczi.multification.notice;


import dev.rollczi.multification.locate.LocaleProvider;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

class LanguageViewersIndex<Viewer> {

    private final Map<Locale, Set<Viewer>> viewersByLanguage;

    private LanguageViewersIndex(Map<Locale, Set<Viewer>> viewersByLanguage) {
        this.viewersByLanguage = viewersByLanguage;
    }

    Set<Locale> getLocales() {
        return this.viewersByLanguage.keySet();
    }

    Set<Viewer> getViewers(Locale language) {
        return this.viewersByLanguage.get(language);
    }

    static <Viewer> LanguageViewersIndex<Viewer> of(LocaleProvider<Viewer> localeProvider, Collection<Viewer> viewers) {
        Map<Locale, Set<Viewer>> viewersByLanguage = new HashMap<>();

        for (Viewer viewer : viewers) {
            viewersByLanguage
                .computeIfAbsent(localeProvider.provide(viewer), key -> new HashSet<>())
                .add(viewer);
        }

        return new LanguageViewersIndex<>(viewersByLanguage);
    }

}
