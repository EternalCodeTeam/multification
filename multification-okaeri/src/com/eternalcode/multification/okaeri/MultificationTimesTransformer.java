package com.eternalcode.multification.okaeri;

import com.eternalcode.multification.notice.NoticeContent.Times;
import eu.okaeri.configs.schema.GenericsPair;
import eu.okaeri.configs.serdes.BidirectionalTransformer;
import eu.okaeri.configs.serdes.ObjectTransformer;
import eu.okaeri.configs.serdes.SerdesContext;
import java.time.Duration;

public class MultificationTimesTransformer extends ObjectTransformer<String, Times> {

    private static final String TIMES = "%s %s %s";

    @Override
    public GenericsPair getPair() {
        return this.genericsPair(String.class, Times.class);
    }

    @Override
    public Times transform(String data, SerdesContext context) {
        String[] split = data.split(" ");
        return new Times(Duration.parse(split[0]), Duration.parse(split[1]), Duration.parse(split[2]));
    }
}