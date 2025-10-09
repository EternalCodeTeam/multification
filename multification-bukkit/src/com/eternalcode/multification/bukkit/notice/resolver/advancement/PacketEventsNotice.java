package com.eternalcode.multification.bukkit.notice.resolver.advancement;

import com.eternalcode.multification.notice.Notice;
import java.time.Duration;

public class PacketEventsNotice {

    /**
     * Creates a simple advancement with title and description
     *
     * @param title Advancement title
     * @param description Advancement description
     * @return Notice instance
     */
    public static Notice advancement(String title, String description) {
        return builder()
                .title(title)
                .description(description)
                .buildAdvancement();
    }

    /**
     * Creates an advancement with title, description and icon
     *
     * @param title Advancement title
     * @param description Advancement description
     * @param icon Icon material/item name
     * @return Notice instance
     */
    public static Notice advancement(String title, String description, String icon) {
        return builder()
                .title(title)
                .description(description)
                .icon(icon)
                .buildAdvancement();
    }

    /**
     * Creates an advancement with title, description, icon and frame type
     *
     * @param title Advancement title
     * @param description Advancement description
     * @param icon Icon material/item name
     * @param frameType Frame type
     * @return Notice instance
     */
    public static Notice advancement(String title, String description, String icon, AdvancementFrameType frameType) {
        return builder()
                .title(title)
                .description(description)
                .icon(icon)
                .frameType(frameType)
                .buildAdvancement();
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Notice.BaseBuilder<Builder> {

        private String title;
        private String description;
        private String icon;
        private AdvancementFrameType frameType;
        private String background;
        private boolean showToast = true;
        private boolean hidden = false;
        private float x = 0.0f;
        private float y = 0.0f;
        private Duration showTime;

        /**
         * Sets the title of the advancement
         *
         * @param title Advancement title
         * @return Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * Sets the description of the advancement
         *
         * @param description Advancement description
         * @return Builder instance
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * Sets the icon for the advancement
         *
         * @param icon Icon material/item name
         * @return Builder instance
         */
        public Builder icon(String icon) {
            this.icon = icon;
            return this;
        }

        /**
         * Sets the frame type of the advancement
         *
         * @param frameType Type of advancement frame
         * @return Builder instance
         */
        public Builder frameType(AdvancementFrameType frameType) {
            this.frameType = frameType;
            return this;
        }

        /**
         * Sets the background texture
         *
         * @param background Background texture path
         * @return Builder instance
         */
        public Builder background(String background) {
            this.background = background;
            return this;
        }

        /**
         * Sets whether to show the toast notification
         *
         * @param showToast Show toast notification
         * @return Builder instance
         */
        public Builder showToast(boolean showToast) {
            this.showToast = showToast;
            return this;
        }

        /**
         * Sets whether the advancement is hidden
         *
         * @param hidden Hidden status
         * @return Builder instance
         */
        public Builder hidden(boolean hidden) {
            this.hidden = hidden;
            return this;
        }

        /**
         * Sets the X position (0.0 = left, 1.0 = right)
         *
         * @param x X coordinate
         * @return Builder instance
         */
        public Builder x(float x) {
            this.x = x;
            return this;
        }

        /**
         * Sets the Y position (0.0 = top, 1.0 = bottom)
         *
         * @param y Y coordinate
         * @return Builder instance
         */
        public Builder y(float y) {
            this.y = y;
            return this;
        }

        /**
         * Sets the position of the advancement
         *
         * @param x X coordinate (0.0 = left, 1.0 = right)
         * @param y Y coordinate (0.0 = top, 1.0 = bottom)
         * @return Builder instance
         */
        public Builder position(float x, float y) {
            this.x = x;
            this.y = y;
            return this;
        }

        /**
         * Sets how long the advancement should be shown
         *
         * @param showTime Duration to show the advancement
         * @return Builder instance
         */
        public Builder showTime(Duration showTime) {
            this.showTime = showTime;
            return this;
        }

        /**
         * Builds the advancement notice
         *
         * @return Notice with advancement content
         */
        public Notice buildAdvancement() {
            return this.withPart(
                    PacketEventsNoticeKey.ADVANCEMENT,
                    new AdvancementContent(
                            title,
                            description,
                            icon,
                            frameType,
                            background,
                            showToast,
                            hidden,
                            x,
                            y,
                            showTime
                    )
            ).build();
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}