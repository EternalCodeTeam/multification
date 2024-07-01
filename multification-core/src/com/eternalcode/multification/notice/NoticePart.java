package com.eternalcode.multification.notice;

import com.eternalcode.multification.notice.resolver.NoticeContent;
import org.jetbrains.annotations.ApiStatus;

public record NoticePart<T extends NoticeContent>(NoticeKey<T> noticeKey, T content) {

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.2.0")
    public NoticeType type() {
        return NoticeType.fromKey(noticeKey.key());
    }

}
