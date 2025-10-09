package com.eternalcode.multification.packetevents.notice.resolver;

import com.eternalcode.multification.notice.NoticeKey;
import com.eternalcode.multification.notice.resolver.NoticeContent;

public interface PacketEventsNoticeKey<T extends NoticeContent> extends NoticeKey<T> {

    NoticeKey<AdvancementContent> ADVANCEMENT = NoticeKey.of("advancement", AdvancementContent.class);
}
