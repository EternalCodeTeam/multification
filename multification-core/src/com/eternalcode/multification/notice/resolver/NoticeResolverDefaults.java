package com.eternalcode.multification.notice.resolver;

import com.eternalcode.commons.scheduler.Scheduler;
import com.eternalcode.multification.notice.resolver.actionbar.ActionbarResolver;
import com.eternalcode.multification.notice.resolver.bossbar.BossBarResolver;
import com.eternalcode.multification.notice.resolver.bossbar.BossBarService;
import com.eternalcode.multification.notice.resolver.chat.ChatResolver;
import com.eternalcode.multification.notice.resolver.sound.SoundAdventureResolver;
import com.eternalcode.multification.notice.resolver.title.SubtitleResolver;
import com.eternalcode.multification.notice.resolver.title.SubtitleWithEmptyTitleResolver;
import com.eternalcode.multification.notice.resolver.title.TimesResolver;
import com.eternalcode.multification.notice.resolver.title.TitleHideResolver;
import com.eternalcode.multification.notice.resolver.title.TitleResolver;
import com.eternalcode.multification.notice.resolver.title.TitleWithEmptySubtitleResolver;

public final class NoticeResolverDefaults {

    private NoticeResolverDefaults() {
    }

    public static NoticeResolverRegistry createRegistry(Scheduler scheduler) {
        return new NoticeResolverRegistry()
            .registerResolver(new ChatResolver())
            .registerResolver(new TitleResolver())
            .registerResolver(new SubtitleResolver())
            .registerResolver(new TitleWithEmptySubtitleResolver())
            .registerResolver(new SubtitleWithEmptyTitleResolver())
            .registerResolver(new TimesResolver())
            .registerResolver(new TitleHideResolver())
            .registerResolver(new SoundAdventureResolver())
            .registerResolver(new ActionbarResolver())
            .registerResolver(new BossBarResolver(new BossBarService(scheduler)))
        ;
    }

}
