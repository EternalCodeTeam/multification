package com.eternalcode.multification.notice.resolver;

import com.eternalcode.multification.notice.NoticeKey;

public record NoticeDeserializeResult<T extends NoticeContent>(NoticeKey<T> noticeKey, T content) {
}
