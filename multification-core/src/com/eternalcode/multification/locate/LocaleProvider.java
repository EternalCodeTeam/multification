package com.eternalcode.multification.locate;

import java.util.Locale;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface LocaleProvider<VIEWER> {

    @NotNull
    Locale provide(VIEWER viewer);

}
