package com.eternalcode.multification.okaeri;

import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.SerdesRegistry;
import org.jetbrains.annotations.NotNull;

public class MultificationSerdesPack implements OkaeriSerdesPack {
    @Override
    public void register(@NotNull SerdesRegistry registry) {
        registry.register(new MultificationNoticeSerializer());
    }
}
