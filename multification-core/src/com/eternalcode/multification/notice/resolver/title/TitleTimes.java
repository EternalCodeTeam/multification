package com.eternalcode.multification.notice.resolver.title;

import com.eternalcode.multification.notice.resolver.NoticeContent;
import java.time.Duration;

public record TitleTimes(Duration fadeIn, Duration stay, Duration fadeOut) implements NoticeContent {
}
