package com.eternalcode.multification.bukkit.notice.resolver.advancement;

import com.eternalcode.multification.notice.Notice;

import java.time.Duration;

public class PacketEventsNotice {

    public static Notice advancement(String title, String description) {
        return PacketEventsNotice.builder()
                .advancement(title, description)
                .build();
    }

    public static Notice advancement(String title, String description, String icon) {
        return PacketEventsNotice.builder()
                .advancement(title, description, icon)
                .build();
    }

    public static Notice advancement(String title, String description, String icon, AdvancementFrameType frameType) {
        return PacketEventsNotice.builder()
                .advancement(title, description, icon, frameType)
                .build();
    }

    public static Notice advancement(String title, String description, String icon, AdvancementFrameType frameType, String background) {
        return PacketEventsNotice.builder()
                .advancement(title, description, icon, frameType, background)
                .build();
    }

    public static Notice advancement(String title, String description, String icon, AdvancementFrameType frameType, Duration showTime) {
        return PacketEventsNotice.builder()
                .advancement(title, description, icon, frameType, showTime)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Notice.BaseBuilder<Builder> {

        public Builder advancement(String title, String description) {
            return this.advancement(title, description, null, null, null, true, true, 0.0f, 0.0f, null);
        }

        public Builder advancement(String title, String description, String icon) {
            return this.advancement(title, description, icon, null, null, true, true, 0.0f, 0.0f, null);
        }

        public Builder advancement(String title, String description, String icon, AdvancementFrameType frameType) {
            return this.advancement(title, description, icon, frameType, null, true, true, 0.0f, 0.0f, null);
        }

        public Builder advancement(String title, String description, String icon, AdvancementFrameType frameType, String background) {
            return this.advancement(title, description, icon, frameType, background, true, true, 0.0f, 0.0f, null);
        }

        public Builder advancement(String title, String description, String icon, AdvancementFrameType frameType, Duration showTime) {
            return this.advancement(title, description, icon, frameType, null, true, true, 0.0f, 0.0f, showTime);
        }

        public Builder advancement(
                String title,
                String description,
                String icon,
                AdvancementFrameType frameType,
                String background,
                boolean showToast,
                boolean hidden,
                float x,
                float y,
                Duration showTime
        ) {
            return this.withPart(
                    PacketEventsNoticeKey.ADVANCEMENT,
                    new AdvancementContent(title, description, icon, frameType, background, showToast, hidden, x, y, showTime)
            );
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}