package dev.rollczi.multification.shared;

@FunctionalInterface
public interface Replacer<Viewer> {

    String apply(Viewer viewer, String text);

}
