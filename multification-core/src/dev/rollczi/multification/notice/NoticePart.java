package dev.rollczi.multification.notice;

public record NoticePart<T extends NoticeContent>(NoticeType type, T content) {
}
