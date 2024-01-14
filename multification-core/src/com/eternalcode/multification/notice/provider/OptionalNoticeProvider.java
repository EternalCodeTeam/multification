package com.eternalcode.multification.notice.provider;

import com.eternalcode.multification.notice.Notice;
import java.util.Optional;

@FunctionalInterface
public interface OptionalNoticeProvider<Translation> {

    Optional<Notice> extract(Translation translation);

}
