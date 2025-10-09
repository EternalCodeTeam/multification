package com.eternalcode.multification.packetevents.notice.resolver;

import com.eternalcode.multification.notice.NoticeKey;
import com.eternalcode.multification.notice.resolver.NoticeSerdesResult;
import com.eternalcode.multification.notice.resolver.text.TextContentResolver;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.advancements.Advancement;
import com.github.retrooper.packetevents.protocol.advancements.AdvancementDisplay;
import com.github.retrooper.packetevents.protocol.advancements.AdvancementHolder;
import com.github.retrooper.packetevents.protocol.advancements.AdvancementProgress;
import com.github.retrooper.packetevents.protocol.advancements.AdvancementType;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAdvancements;
import java.time.Duration;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.UnaryOperator;

public class AdvancementResolver implements TextContentResolver<AdvancementContent> {

    private static final String FORMAT = "%s|%s|%s|%s|%s|%b|%b|%f|%f|%s";

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

        String advancementKeyString = "toast_" + UUID.randomUUID().toString().replace("-", "");
        Key advancementKey = Key.key(this.plugin.getName().toLowerCase(), advancementKeyString);

        Component titleComponent = serializer.deserialize(content.title());
        Component descComponent = serializer.deserialize(content.description());

        ItemStack icon = this.createIcon(content.iconOrDefault());

        ResourceLocation background = this.parseBackground(content.background());

        AdvancementDisplay display = new AdvancementDisplay(
                titleComponent,
                descComponent,
                icon,
                content.frameTypeOrDefault(),
                background,
                content.showToast(),
                content.hidden(),
                content.x(),
                content.y()
        );

        Advancement advancement = new Advancement(
                null,
                display,
                Collections.emptyList(),
                false
        );

        List<AdvancementHolder> holders = new ArrayList<>();
        ResourceLocation advancementLocation = new ResourceLocation(advancementKey.namespace(), advancementKey.value());
        holders.add(new AdvancementHolder(advancementLocation, advancement));

        WrapperPlayServerUpdateAdvancements addPacket = new WrapperPlayServerUpdateAdvancements(
                false,
                holders,
                Collections.emptySet(),
                Collections.emptyMap(),
                true
        );
        user.sendPacket(addPacket);

        Map<ResourceLocation, AdvancementProgress> progressMap = new HashMap<>();
        progressMap.put(advancementLocation, new AdvancementProgress(new HashMap<>()));

        WrapperPlayServerUpdateAdvancements grantPacket = new WrapperPlayServerUpdateAdvancements(
                false,
                Collections.emptyList(),
                Collections.emptySet(),
                progressMap,
                false
        );
        user.sendPacket(grantPacket);

        long delayTicks = content.showTimeOrDefault().toMillis() / 50;

        Bukkit.getScheduler().runTaskLater(
                this.plugin,
                () -> {
                    Set<ResourceLocation> removeKeys = new HashSet<>();
                    removeKeys.add(new ResourceLocation(advancementKey.namespace(), advancementKey.value()));

                    WrapperPlayServerUpdateAdvancements removePacket = new WrapperPlayServerUpdateAdvancements(
                            false,
                            Collections.emptyList(),
                            removeKeys,
                            Collections.emptyMap(),
                            false
                    );
                    user.sendPacket(removePacket);
                },
                delayTicks
        );
    }

    @Override
    public NoticeSerdesResult serialize(AdvancementContent content) {
        return new NoticeSerdesResult.Single(String.format(FORMAT,
                content.title(),
                content.description(),
                content.iconOrDefault(),
                content.frameTypeOrDefault().name(),
                content.background() != null ? content.background() : "",
                content.showToast(),
                content.hidden(),
                content.x(),
                content.y(),
                content.showTimeOrDefault().toMillis()
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
            String icon = parts.length > 2 && !parts[2].isEmpty() ? parts[2] : null;
            AdvancementType frameType = parts.length > 3 && !parts[3].isEmpty()
                    ? AdvancementType.valueOf(parts[3])
                    : null;
            String background = parts.length > 4 && !parts[4].isEmpty() ? parts[4] : null;
            boolean showToast = parts.length > 5 ? Boolean.parseBoolean(parts[5]) : AdvancementContent.DEFAULT_SHOW_TOAST;
            boolean hidden = parts.length > 6 ? Boolean.parseBoolean(parts[6]) : AdvancementContent.DEFAULT_HIDDEN;
            float x = parts.length > 7 ? Float.parseFloat(parts[7]) : AdvancementContent.DEFAULT_X;
            float y = parts.length > 8 ? Float.parseFloat(parts[8]) : AdvancementContent.DEFAULT_Y;
            Duration showTime = parts.length > 9 && !parts[9].isEmpty()
                    ? Duration.ofMillis(Long.parseLong(parts[9]))
                    : null;

            return new AdvancementContent(title, description, icon, frameType, background, showToast, hidden, x, y, showTime);
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
                content.frameType(),
                content.background(),
                content.showToast(),
                content.hidden(),
                content.x(),
                content.y(),
                content.showTime()
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
     * Parses ResourceLocation from string format "namespace:path".
     * Examples:
     * - "minecraft:textures/gui/advancements/backgrounds/stone.png"
     * - "mypack:custom/background.png"
     *
     * @param backgroundString the background string in format "namespace:path"
     * @return ResourceLocation or null if string is null/invalid
     */
    private ResourceLocation parseBackground(@Nullable String backgroundString) {
        if (backgroundString == null || backgroundString.isEmpty()) {
            return null;
        }

        // Split by colon to get namespace and path
        String[] parts = backgroundString.split(":", 2);

        if (parts.length != 2) {
            // Invalid format, default to minecraft namespace
            return new ResourceLocation("minecraft", backgroundString);
        }

        return new ResourceLocation(parts[0], parts[1]);
    }
}