package com.eternalcode.multification.bukkit.notice.resolver.advancement;

import com.eternalcode.multification.notice.resolver.text.TextContent;
import java.time.Duration;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public record AdvancementContent(
        String title,
        String description,
        @Nullable String icon,
        @Nullable AdvancementFrameType frameType,
        @Nullable String background,
        boolean showToast,
        boolean hidden,
        float x,
        float y,
        @Nullable Duration showTime
) implements TextContent {

    public static final String DEFAULT_ICON = "GRASS_BLOCK";
    public static final AdvancementFrameType DEFAULT_FRAME = AdvancementFrameType.TASK;
    public static final boolean DEFAULT_SHOW_TOAST = true;
    public static final boolean DEFAULT_HIDDEN = true;
    public static final float DEFAULT_X = 0.0f;
    public static final float DEFAULT_Y = 0.0f;
    public static final Duration DEFAULT_SHOW_TIME = Duration.ofSeconds(1);

    public AdvancementContent(String title, String description, @Nullable String icon, @Nullable AdvancementFrameType frameType) {
        this(title, description, icon, frameType, null, DEFAULT_SHOW_TOAST, DEFAULT_HIDDEN, DEFAULT_X, DEFAULT_Y, null);
    }

    @Override
    public List<String> contents() {
        return List.of(this.title, this.description);
    }

    public String iconOrDefault() {
        return this.icon != null ? this.icon : DEFAULT_ICON;
    }

    public AdvancementFrameType frameTypeOrDefault() {
        return this.frameType != null ? this.frameType : DEFAULT_FRAME;
    }

    public Duration showTimeOrDefault() {
        return this.showTime != null ? this.showTime : DEFAULT_SHOW_TIME;
    }
}