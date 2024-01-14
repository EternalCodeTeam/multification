package com.eternalcode.multification.shared;

@FunctionalInterface
public interface Replacer<Viewer> {

    String apply(Viewer viewer, String text);

}
