package com.eternalcode.multification.packetevents.resolver.advancement;

import com.eternalcode.multification.notice.resolver.text.TextContent;
import com.github.retrooper.packetevents.protocol.advancements.AdvancementType;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public record AdvancementContent(
        String title,
        String description,
        @Nullable String icon,
        @Nullable AdvancementType frameType,
        @Nullable String background,
        boolean showToast,
        boolean hidden,
        float x,
        float y
) implements TextContent {

    public static final String DEFAULT_ICON = "GRASS_BLOCK";
    public static final AdvancementType DEFAULT_FRAME = AdvancementType.TASK;
    public static final boolean DEFAULT_SHOW_TOAST = true;
    public static final boolean DEFAULT_HIDDEN = true;
    public static final float DEFAULT_X = 0.0f;
    public static final float DEFAULT_Y = 0.0f;

    public AdvancementContent(String title, String description, @Nullable String icon, @Nullable AdvancementType frameType) {
        this(title, description, icon, frameType, null, DEFAULT_SHOW_TOAST, DEFAULT_HIDDEN, DEFAULT_X, DEFAULT_Y);
    }

    @Override
    public List<String> contents() {
        return List.of(this.title, this.description);
    }

    public String iconOrDefault() {
        return this.icon != null ? this.icon : DEFAULT_ICON;
    }

    public AdvancementType frameTypeOrDefault() {
        return this.frameType != null ? this.frameType : DEFAULT_FRAME;
    }
}