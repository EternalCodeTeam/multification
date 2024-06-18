package com.eternalcode.multification.okaeri;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.SerdesRegistry;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;

@Experimental
public class MultificationSerdesPack implements OkaeriSerdesPack {
    @Override
    public void register(@NotNull SerdesRegistry registry) {
        registry.register(new MultificationTimesTransformer());
        registry.register(new MultificationMusicTransformer());
        registry.register(new MultificationNoticeSerializer());
    }
}