package com.eternalcode.multification.okaeri;
import com.eternalcode.multification.Multification;
import com.eternalcode.multification.notice.resolver.NoticeResolverRegistry;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.SerdesRegistry;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;

public class MultificationSerdesPack implements OkaeriSerdesPack {

    private final NoticeResolverRegistry noticeRegistry;

    public MultificationSerdesPack(NoticeResolverRegistry noticeRegistry) {
        this.noticeRegistry = noticeRegistry;
    }

    public MultificationSerdesPack(Multification<?, ?> multification) {
        this.noticeRegistry = multification.getNoticeRegistry();
    }

    @Override
    public void register(@NotNull SerdesRegistry registry) {
        registry.register(new MultificationNoticeSerializer(this.noticeRegistry));
    }
}