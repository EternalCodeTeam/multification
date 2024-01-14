package dev.rollczi.multification.notice.provider;

import dev.rollczi.multification.notice.Notice;
import java.util.Optional;

@FunctionalInterface
public interface OptionalNoticeProvider<Translation> {

    Optional<Notice> extract(Translation translation);

}
