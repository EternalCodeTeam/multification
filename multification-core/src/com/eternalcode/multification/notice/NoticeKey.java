package com.eternalcode.multification.notice;

import com.eternalcode.multification.notice.resolver.NoticeContent;
import com.eternalcode.multification.notice.resolver.actionbar.ActionbarContent;
import com.eternalcode.multification.notice.resolver.bossbar.BossBarContent;
import com.eternalcode.multification.notice.resolver.chat.ChatContent;
import com.eternalcode.multification.notice.resolver.sound.SoundAdventure;
import com.eternalcode.multification.notice.resolver.title.TitleContent;
import com.eternalcode.multification.notice.resolver.title.TitleHide;
import com.eternalcode.multification.notice.resolver.title.TitleTimes;

public interface NoticeKey<T extends NoticeContent> {

    NoticeKey<ChatContent> CHAT = NoticeKey.of("chat", ChatContent.class);
    NoticeKey<ActionbarContent> ACTION_BAR = NoticeKey.of("actionbar", ActionbarContent.class);
    NoticeKey<TitleContent> TITLE = NoticeKey.of("title", TitleContent.class);
    NoticeKey<TitleContent> SUBTITLE = NoticeKey.of("subtitle", TitleContent.class);
    NoticeKey<TitleTimes> TITLE_TIMES = NoticeKey.of("times", TitleTimes.class);
    NoticeKey<TitleHide> TITLE_HIDE = NoticeKey.of("hideTitle", TitleHide.class);
    NoticeKey<SoundAdventure> SOUND = NoticeKey.of("sound", SoundAdventure.class);
    NoticeKey<BossBarContent> BOSS_BAR = NoticeKey.of("bossBar", BossBarContent.class);

    String key();

    Class<T> type();

    static <T extends NoticeContent> NoticeKey<T> of(String text, Class<T> type) {
        return new NoticeKeyImpl<>(text, type);
    }

}
