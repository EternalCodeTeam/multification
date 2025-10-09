package main.java.com.eternalcode.multification.packetevents;

import com.eternalcode.multification.notice.NoticeKey;
import com.eternalcode.multification.notice.resolver.NoticeSerdesResult;
import com.eternalcode.multification.notice.resolver.advancement.AdvancementContent;
import com.eternalcode.multification.notice.resolver.advancement.AdvancementFrameType;
import com.eternalcode.multification.notice.resolver.advancement.PacketEventsNoticeKey;
import com.eternalcode.multification.notice.resolver.text.TextContentResolver;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
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

        // Generate unique advancement key for this toast
        String advancementKey = "toast_" + UUID.randomUUID().toString().replace("-", "");

        Component titleComponent = serializer.deserialize(content.title());
        Component descComponent = serializer.deserialize(content.description());

        ItemStack icon = this.createIcon(content.iconOrDefault());
        int flags = 0x01; // SHOW_TOAST flag

        WrapperPlayServerAdvancements.AdvancementDisplay display = new WrapperPlayServerAdvancements.AdvancementDisplay(
                titleComponent,
                descComponent,
                icon,
                this.toFrameTypeOrdinal(content.frameTypeOrDefault()),
                flags,
                Optional.empty(),
                0.0f,
                0.0f
        );

        WrapperPlayServerAdvancements.Advancement advancement = new WrapperPlayServerAdvancements.Advancement(
                Optional.empty(),
                Optional.of(display),
                new ArrayList<>(),
                Optional.empty()
        );

        Map<String, WrapperPlayServerAdvancements.Advancement> advancements = new HashMap<>();
        advancements.put(advancementKey, advancement);

        // Packet 1: Add advancement to client
        WrapperPlayServerAdvancements addPacket = new WrapperPlayServerAdvancements(
                false,
                advancements,
                new ArrayList<>(),
                new HashMap<>()
        );
        user.sendPacket(addPacket);

        // Packet 2: Grant criteria to trigger toast display
        Map<String, WrapperPlayServerAdvancements.AdvancementProgress> progressMap = new HashMap<>();
        Map<String, Long> criteria = new HashMap<>();
        criteria.put("trigger", System.currentTimeMillis());

        progressMap.put(advancementKey, new WrapperPlayServerAdvancements.AdvancementProgress(criteria));

        WrapperPlayServerAdvancements grantPacket = new WrapperPlayServerAdvancements(
                false,
                new HashMap<>(),
                new ArrayList<>(),
                progressMap
        );
        user.sendPacket(grantPacket);

        Bukkit.getScheduler().runTaskLater(
                this.plugin,
                () -> {
                    WrapperPlayServerAdvancements removePacket = new WrapperPlayServerAdvancements(
                            false,
                            new HashMap<>(),
                            Collections.singletonList(advancementKey),
                            new HashMap<>()
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
            AdvancementFrameType frameType = parts.length > 3
                    ? AdvancementFrameType.valueOf(parts[3])
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

    /**
     * Converts AdvancementFrameType enum to protocol ordinal.
     * PacketEvents uses int ordinal instead of enum for FrameType.
     *
     * @param frameType the frame type enum
     * @return protocol ordinal (0 = TASK, 1 = CHALLENGE, 2 = GOAL)
     */
    private int toFrameTypeOrdinal(AdvancementFrameType frameType) {
        return switch (frameType) {
            case TASK -> 0;       // Yellow frame (normal achievement)
            case CHALLENGE -> 1;  // Purple frame (challenge)
            case GOAL -> 2;       // Rounded frame (goal)
        };
    }
}