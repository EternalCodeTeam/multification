package com.eternalcode.multification.notice;

import com.eternalcode.multification.notice.resolver.NoticeContent;
import com.eternalcode.multification.notice.resolver.actionbar.ActionbarContent;
import com.eternalcode.multification.notice.resolver.sound.SoundAdventure;
import com.eternalcode.multification.notice.resolver.chat.ChatContent;
import com.eternalcode.multification.notice.resolver.title.TitleContent;
import com.eternalcode.multification.notice.resolver.title.TitleHide;
import com.eternalcode.multification.notice.resolver.title.TitleTimes;
import org.jetbrains.annotations.ApiStatus;

@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "1.2.0")
public enum NoticeType implements NoticeKey {
    CHAT(ChatContent.class, NoticeKey.CHAT.key()),
    ACTION_BAR(ActionbarContent.class, NoticeKey.ACTION_BAR.key()),
    TITLE(TitleContent.class, NoticeKey.TITLE.key()),
    SUBTITLE(TitleContent.class, NoticeKey.SUBTITLE.key()),

    // TODO: Find a better sotultion, minecraft sends subtitle/title only when the second part also contains some part
    TITLE_WITH_EMPTY_SUBTITLE(TitleContent.class, "titleWithEmptySubtitle"),
    SUBTITLE_WITH_EMPTY_TITLE(TitleContent.class, "subtitleWithEmptyTitle"),

    TITLE_TIMES(TitleTimes.class, NoticeKey.TITLE_TIMES.key()),
    TITLE_HIDE(TitleHide.class, NoticeKey.TITLE_HIDE.key()),
    SOUND(SoundAdventure.class, NoticeKey.SOUND.key());

    private final Class<?> inputType;
    private final String name;

    NoticeType(Class<?> inputType, String name) {
        this.inputType = inputType;
        this.name = name;
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.2.0")
    public String getKey() {
        return this.name;
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.2.0")
    public Class<?> contentType() {
        return this.inputType;
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.2.0")
    public <T extends NoticeContent> NoticeKey<T> toNoticeKey() {
        return NoticeKey.of(this.getKey(), (Class<T>) contentType());
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.2.0")
    public static NoticeType fromKey(String key) {
        for (NoticeType type : values()) {
            if (type.getKey().equals(key)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown notice type: " + key);
    }

    @Override
    public String key() {
        return this.getKey();
    }

    @Override
    public Class<?> type() {
        return this.contentType();
    }

}
