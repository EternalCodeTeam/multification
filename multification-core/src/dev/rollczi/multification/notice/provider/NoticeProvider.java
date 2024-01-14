package dev.rollczi.multification.notice.provider;


import dev.rollczi.multification.notice.Notice;

@FunctionalInterface
public interface NoticeProvider<Translation> {

    Notice extract(Translation translation);

}
