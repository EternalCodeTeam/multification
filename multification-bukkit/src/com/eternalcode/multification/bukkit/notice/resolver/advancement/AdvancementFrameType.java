package com.eternalcode.multification.bukkit.notice.resolver.advancement;

import com.github.retrooper.packetevents.protocol.advancements.AdvancementType;

public enum AdvancementFrameType {

    TASK,
    CHALLENGE,
    GOAL;

    public AdvancementType toPacketEventsType() {
        return AdvancementType.valueOf(this.name());
    }
}

