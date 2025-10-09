package com.eternalcode.multification.bukkit.notice.resolver.advancement;

import com.eternalcode.multification.notice.NoticeKey;
import com.eternalcode.multification.notice.resolver.NoticeContent;

public interface PacketEventsNoticeKey<T extends NoticeContent> extends NoticeKey<T> {

    NoticeKey<AdvancementContent> ADVANCEMENT = NoticeKey.of("advancement", AdvancementContent.class);
}
