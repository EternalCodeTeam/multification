package com.eternalcode.multification;

import com.eternalcode.multification.adventure.ComponentUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

class AudienceMock implements Audience {

    private static final Map<UUID, List<String>> MESSAGES = new HashMap<>();

    private final UUID uuid;

    public AudienceMock(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void sendMessage(final @NotNull Identity source, final @NotNull Component message, final @NotNull MessageType type) {
        MESSAGES.computeIfAbsent(uuid, key -> new ArrayList<>()).add(ComponentUtil.componentToText(message));
    }

    static List<String> getMessages(UUID uuid) {
        return Collections.unmodifiableList(MESSAGES.getOrDefault(uuid, new ArrayList<>()));
    }

}
