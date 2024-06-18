package com.eternalcode.multification.okaeri;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import eu.okaeri.configs.serdes.SerdesRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MultificationSerdesPackTest {

    @DisplayName("Test registry registration")
    @Test
    void register() {
        SerdesRegistry registry = mock(SerdesRegistry.class);

        MultificationSerdesPack pack = new MultificationSerdesPack();
        pack.register(registry);

        verify(registry, times(1)).register(any(MultificationTimesTransformer.class));
        verify(registry, times(1)).register(any(MultificationMusicTransformer.class));
        verify(registry, times(1)).register(any(MultificationNoticeSerializer.class));
    }
}