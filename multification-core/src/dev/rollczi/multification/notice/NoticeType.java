package dev.rollczi.multification.notice;

import static dev.rollczi.multification.notice.NoticeContent.Music;
import static dev.rollczi.multification.notice.NoticeContent.None;
import static dev.rollczi.multification.notice.NoticeContent.Text;
import static dev.rollczi.multification.notice.NoticeContent.Times;

public enum NoticeType {
    CHAT(Text.class, "chat"),
    ACTION_BAR(Text.class, "actionbar"),
    TITLE(Text.class, "title"),
    SUBTITLE(Text.class, "subtitle"),
    TITLE_TIMES(Times.class, "times"),
    TITLE_HIDE(None.class, "titleHide"),
    SOUND(Music.class, "sound");

    private final Class<?> inputType;
    private final String name;

    NoticeType(Class<?> inputType, String name) {
        this.inputType = inputType;
        this.name = name;
    }

    public String getKey() {
        return this.name;
    }

    public Class<?> contentType() {
        return this.inputType;
    }

    public static NoticeType fromKey(String key) {
        for (NoticeType type : values()) {
            if (type.getKey().equals(key)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown notice type: " + key);
    }

}
