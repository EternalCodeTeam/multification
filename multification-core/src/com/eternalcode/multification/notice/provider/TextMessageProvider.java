package com.eternalcode.multification.notice.provider;

@FunctionalInterface
public interface TextMessageProvider<Translation> {

    String extract(Translation translation);

}
