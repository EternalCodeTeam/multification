package com.eternalcode.multification.bukkit.notice;

import com.eternalcode.multification.bukkit.notice.resolver.sound.SoundBukkit;
import com.eternalcode.multification.notice.Notice;
import com.eternalcode.multification.notice.NoticePart;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

public class BukkitNotice {

    public static Notice sound(Sound sound, SoundCategory category, float volume, float pitch) {
        return BukkitNotice.builder()
            .sound(sound, category, pitch, volume)
            .build();
    }

    public static Notice sound(Sound sound, float volume, float pitch) {
        return BukkitNotice.builder()
            .sound(sound, pitch, volume)
            .build();
    }

    public static BukkitNotice.Builder builder() {
        return new Builder();
    }

    public static class Builder extends Notice.BaseBuilder<Builder> {

        public Builder sound(Sound sound, SoundCategory category, float volume, float pitch) {
            return this.withPart(BukkitNoticeKey.SOUND, new SoundBukkit(sound, category, pitch, volume));
        }

        public Builder sound(Sound sound, float volume, float pitch) {
            return this.withPart(BukkitNoticeKey.SOUND, new SoundBukkit(sound, null, pitch, volume));
        }

        @Override
        protected Builder getThis() {
            return this;
        }

    }


}
