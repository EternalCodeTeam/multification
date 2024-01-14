package dev.rollczi.multification.translation;

import java.util.Locale;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface TranslationProvider<Translation> {

    @NotNull
    Translation provide(Locale locale);

}
