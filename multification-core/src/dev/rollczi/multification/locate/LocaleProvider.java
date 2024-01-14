package dev.rollczi.multification.locate;

import java.util.Locale;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface LocaleProvider<Viewer> {

    @NotNull
    Locale provide(Viewer viewer);

}
