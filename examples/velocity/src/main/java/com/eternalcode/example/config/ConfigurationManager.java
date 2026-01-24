package com.eternalcode.example.config;

import com.eternalcode.multification.cdn.MultificationNoticeCdnComposer;
import com.eternalcode.multification.notice.Notice;
import com.eternalcode.multification.notice.resolver.NoticeResolverRegistry;
import net.dzikoysk.cdn.Cdn;
import net.dzikoysk.cdn.CdnFactory;
import net.dzikoysk.cdn.source.Source;

import java.io.File;

public class ConfigurationManager {

    private final File dataFolder;
    private final Cdn cdn;
    private MessagesConfig messagesConfig;

    public ConfigurationManager(File dataFolder, NoticeResolverRegistry noticeRegistry) {
        this.dataFolder = dataFolder;
        this.cdn = CdnFactory.createYamlLike()
                .getSettings()
                .withComposer(Notice.class, new MultificationNoticeCdnComposer(noticeRegistry))
                .build();
    }

    public void load(MessagesConfig config, String fileName) {
        this.messagesConfig = config;
        File file = new File(this.dataFolder, fileName);

        this.cdn.load(Source.of(file), config)
                .orThrow(cause -> cause);

        this.cdn.render(config, Source.of(file))
                .orThrow(cause -> cause);
    }

    public void reload() {
        if (this.messagesConfig != null) {
            load(this.messagesConfig, "messages.yml");
        }
    }
}
