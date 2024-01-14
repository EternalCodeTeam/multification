package com.eternalcode.multification.notice;

public record NoticePart<T extends NoticeContent>(NoticeType type, T content) {
}
