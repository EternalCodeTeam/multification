package com.eternalcode.multification.notice.resolver.advancement;

import com.eternalcode.multification.notice.resolver.text.TextContent;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public record AdvancementContent(
        String title,
        String description,
        @Nullable String icon,
        @Nullable AdvancementFrameType frameType
) implements TextContent {

    public static final String DEFAULT_ICON = "GRASS_BLOCK";
    public static final AdvancementFrameType DEFAULT_FRAME = AdvancementFrameType.TASK;

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
}