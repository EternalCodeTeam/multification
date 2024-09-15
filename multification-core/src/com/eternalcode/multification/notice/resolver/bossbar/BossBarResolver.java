package com.eternalcode.multification.notice.resolver.bossbar;

import com.eternalcode.multification.notice.NoticeKey;
import com.eternalcode.multification.notice.resolver.NoticeSerdesResult;
import com.eternalcode.multification.notice.resolver.text.TextContentResolver;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

import java.util.List;
import java.util.Optional;

public class BossBarResolver implements TextContentResolver<BossBarContent> {

    private final NoticeKey<BossBarContent> key;

    public BossBarResolver() {
        this.key = NoticeKey.BOSS_BAR;
    }

    @Override
    public NoticeKey<BossBarContent> noticeKey() {
        return this.key;
    }

    @Override
    public BossBarContent createFromText(List<String> contents) {
        return null;
    }

    @Override
    public void send(Audience audience, ComponentSerializer<Component, Component, String> componentSerializer, BossBarContent content) {

    }

    @Override
    public NoticeSerdesResult serialize(BossBarContent content) {
        return null;
    }

    @Override
    public Optional<BossBarContent> deserialize(NoticeSerdesResult result) {
        return Optional.empty();
    }
}
