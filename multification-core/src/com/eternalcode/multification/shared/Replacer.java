package com.eternalcode.multification.shared;

@FunctionalInterface
public interface Replacer<VIEWER> {

    String apply(VIEWER viewer, String text);

}
