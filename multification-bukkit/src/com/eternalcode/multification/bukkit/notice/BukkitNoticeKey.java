package com.eternalcode.multification.bukkit.notice;

import com.eternalcode.multification.bukkit.notice.resolver.sound.SoundBukkit;
import com.eternalcode.multification.notice.resolver.NoticeContent;
import com.eternalcode.multification.notice.NoticeKey;

public interface BukkitNoticeKey<T extends NoticeContent> extends NoticeKey<T> {

    NoticeKey<SoundBukkit> SOUND = NoticeKey.of("sound", SoundBukkit.class);

}
