package com.eternalcode.multification.notice.provider;


import com.eternalcode.multification.notice.Notice;

@FunctionalInterface
public interface NoticeProvider<Translation> {

    Notice extract(Translation translation);

}
