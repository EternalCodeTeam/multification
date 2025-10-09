package com.eternalcode.multification.packetevents.notice.resolver;

import com.github.retrooper.packetevents.protocol.advancements.AdvancementType;

public enum AdvancementFrameType {

    TASK,
    CHALLENGE,
    GOAL;

    public AdvancementType toPacketEventsType() {
        return AdvancementType.valueOf(this.name());
    }
}

