package com.eternalcode.multification.translation;

import java.util.Locale;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface TranslationProvider<TRANSLATION> {

    @NotNull
    TRANSLATION provide(Locale locale);

}
