package com.eternalcode.multification.notice.resolver.bossbar;

import net.kyori.adventure.bossbar.BossBar;

public class BossBarTask implements Runnable {

    private final BossBarService bossBarService;

    public BossBarTask(BossBarService bossBarService) {
        this.bossBarService = bossBarService;
    }

    @Override
    public void run() {
        for (BossBarEntity bossBarEntity : this.bossBarService.getBossBars()) {
            BossBar bossBar = bossBarEntity.bossBar();

            if (bossBarEntity.isExpired()) {
                this.bossBarService.remove(bossBarEntity.id());
                // TODO: Remove boss bar
                continue;
            }

            bossBar.progress(bossBarEntity.progress());


        }
    }
}
