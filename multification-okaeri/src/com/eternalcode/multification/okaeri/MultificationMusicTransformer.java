package com.eternalcode.multification.okaeri;

import com.eternalcode.multification.notice.NoticeContent.Music;
import eu.okaeri.configs.schema.GenericsPair;
import eu.okaeri.configs.serdes.ObjectTransformer;
import eu.okaeri.configs.serdes.SerdesContext;

public class MultificationMusicTransformer extends ObjectTransformer<String, Music> {

    @Override
    public GenericsPair<String, Music> getPair() {
        return this.genericsPair(String.class, Music.class);
    }

    @Override
    public Music transform(String data, SerdesContext context) {
        String[] split = data.split(" ");

        if (split.length == 4) {
            return new Music(null, null, Float.parseFloat(split[2]), Float.parseFloat(split[3]));
        }

        return new Music(null, null, Float.parseFloat(split[1]), Float.parseFloat(split[2]));
    }
}
