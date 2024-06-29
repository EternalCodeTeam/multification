package com.eternalcode.multification.notice;

import com.eternalcode.multification.notice.resolver.NoticeContent;

record NoticeKeyImpl<T extends NoticeContent>(String key, Class<T> type) implements NoticeKey<T> {
}
