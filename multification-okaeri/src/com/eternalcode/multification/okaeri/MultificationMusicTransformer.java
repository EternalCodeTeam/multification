package com.eternalcode.multification.okaeri;

import com.eternalcode.multification.notice.NoticeContent.Music;
import eu.okaeri.configs.schema.GenericsPair;
import eu.okaeri.configs.serdes.BidirectionalTransformer;
import eu.okaeri.configs.serdes.SerdesContext;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Experimental
public class MultificationMusicTransformer extends BidirectionalTransformer<String, Music> {

    @Override
    public GenericsPair<String, Music> getPair() {
        return this.genericsPair(String.class, Music.class);
    }

    @Override
    public Music leftToRight(String data, @NotNull SerdesContext serdesContext) {
        String[] split = data.split(" ");

        if (split.length == 4) {
            return new Music(
                Sound.valueOf(split[0]),
                SoundCategory.valueOf(split[1]),
                Float.parseFloat(split[2]),
                Float.parseFloat(split[3]));
        }

        return new Music(
            Sound.valueOf(split[0]),
            SoundCategory.MASTER,
            Float.parseFloat(split[2]),
            Float.parseFloat(split[3]));
    }

    @Override
    public String rightToLeft(Music data, @NotNull SerdesContext serdesContext) {
        @Nullable Sound sound = data.sound();
        @Nullable SoundCategory category = data.category();
        float volume = data.volume();
        float pitch = data.pitch();

        if (category == null) {
            return sound.name() + " " + pitch + " " + volume;
        }

        return sound.name() + " " + category + " " + pitch + " " + volume;
    }
}