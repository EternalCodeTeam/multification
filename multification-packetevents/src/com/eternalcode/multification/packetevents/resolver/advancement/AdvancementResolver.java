package com.eternalcode.multification.packetevents.resolver.advancement;

import com.eternalcode.multification.notice.NoticeKey;
import com.eternalcode.multification.notice.resolver.NoticeSerdesResult;
import com.eternalcode.multification.notice.resolver.text.TextContentResolver;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.advancements.Advancement;
import com.github.retrooper.packetevents.protocol.advancements.AdvancementDisplay;
import com.github.retrooper.packetevents.protocol.advancements.AdvancementType;
import com.github.retrooper.packetevents.protocol.advancements.AdvancementProgress;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAdvancements;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class AdvancementResolver implements TextContentResolver<AdvancementContent> {

    private static final String FORMAT = "%s|%s|%s|%s";
    private static final long CLEANUP_DELAY_TICKS = 20L;

    private final NoticeKey<AdvancementContent> key;
    private final Plugin plugin;

    public AdvancementResolver(Plugin plugin) {
        this.key = PacketEventsNoticeKey.ADVANCEMENT;
        this.plugin = plugin;
    }

    @Override
    public NoticeKey<AdvancementContent> noticeKey() {
        return this.key;
    }

    @Override
    public void send(Audience audience, ComponentSerializer<Component, Component, String> serializer, AdvancementContent content) {
        if (!(audience instanceof Player player)) {
            return;
        }

        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        if (user == null) {
            return;
        }

        String advancementKey = "minecraft:toast_" + UUID.randomUUID().toString().replace("-", "");

        Component titleComponent = serializer.deserialize(content.title());
        Component descComponent = serializer.deserialize(content.description());

        ItemStack icon = this.createIcon(content.iconOrDefault());

        AdvancementDisplay display = new AdvancementDisplay(
                titleComponent,
                descComponent,
                icon,
                content.frameTypeOrDefault(),
                null,
                false,
                true,
                100,
                199
        );

        Advancement advancement = new Advancement(
                null,
                display,
                Collections.emptyList(),
                false
        );

        Map<String, Advancement> advancements = new HashMap<>();
        advancements.put(advancementKey, advancement);

        WrapperPlayServerUpdateAdvancements addPacket = new WrapperPlayServerUpdateAdvancements(
                null
        );
        user.sendPacket(addPacket);

        Map<String, AdvancementProgress> progressMap = new HashMap<>();
        progressMap.put(advancementKey, new AdvancementProgress(new HashMap<>()));

        WrapperPlayServerUpdateAdvancements grantPacket = new WrapperPlayServerUpdateAdvancements(
                null
        );
        user.sendPacket(grantPacket);

        Bukkit.getScheduler().runTaskLater(
                this.plugin,
                () -> {
                    WrapperPlayServerUpdateAdvancements removePacket = new WrapperPlayServerUpdateAdvancements(
                            null
                    );
                    user.sendPacket(removePacket);
                },
                CLEANUP_DELAY_TICKS
        );
    }

    @Override
    public NoticeSerdesResult serialize(AdvancementContent content) {
        return new NoticeSerdesResult.Single(String.format(FORMAT,
                content.title(),
                content.description(),
                content.iconOrDefault(),
                content.frameTypeOrDefault().name()
        ));
    }

    @Override
    public Optional<AdvancementContent> deserialize(NoticeSerdesResult result) {
        return result.firstElement().map(value -> {
            String[] parts = value.split("\\|");

            if (parts.length < 2) {
                throw new IllegalArgumentException("Invalid advancement format: " + value);
            }

            String title = parts[0];
            String description = parts[1];
            String icon = parts.length > 2 ? parts[2] : null;
            AdvancementType frameType = parts.length > 3
                    ? AdvancementType.valueOf(parts[3])
                    : null;

            return new AdvancementContent(title, description, icon, frameType);
        });
    }

    @Override
    public AdvancementContent createFromText(List<String> contents) {
        if (contents.isEmpty()) {
            return new AdvancementContent("", "", null, null);
        }

        if (contents.size() == 1) {
            return new AdvancementContent(contents.get(0), "", null, null);
        }

        return new AdvancementContent(contents.get(0), contents.get(1), null, null);
    }

    @Override
    public AdvancementContent applyText(AdvancementContent content, UnaryOperator<String> function) {
        return new AdvancementContent(
                function.apply(content.title()),
                function.apply(content.description()),
                content.icon(),
                content.frameType()
        );
    }

    /**
     * Creates an ItemStack icon from material name string.
     * Falls back to GRASS_BLOCK if material is invalid.
     *
     * @param materialName the material name (e.g., "OAK_SAPLING", "DIAMOND")
     * @return ItemStack with the specified material type
     */
    private ItemStack createIcon(@NotNull String materialName) {
        try {
            ItemType materialType = ItemTypes.getByName(materialName);

            if (materialType == null) {
                throw new IllegalArgumentException("Invalid material: " + materialName);
            }

            return ItemStack.builder()
                    .type(materialType)
                    .build();
        }
        catch (IllegalArgumentException e) {
            return ItemStack.builder()
                    .type(ItemTypes.GRASS_BLOCK)
                    .amount(1)
                    .build();
        }
    }
}