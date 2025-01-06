package com.eternalcode.example.bukkit.config;

import com.eternalcode.multification.cdn.MultificationNoticeCdnComposer;
import com.eternalcode.multification.notice.Notice;
import com.eternalcode.multification.notice.resolver.NoticeResolverRegistry;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import net.dzikoysk.cdn.Cdn;
import net.dzikoysk.cdn.CdnFactory;
import net.dzikoysk.cdn.reflect.Visibility;
import net.dzikoysk.cdn.source.Source;
public class ConfigurationManager {

    private final Cdn cdn;
    private final File dataFolder;
    private final Set<ReloadableConfig> configs = new HashSet<>();

    public ConfigurationManager(File dataFolder, NoticeResolverRegistry resolverRegistry) {
        this.dataFolder = dataFolder;
        this.cdn = CdnFactory
            .createYamlLike()
            .getSettings()
            .withComposer(Notice.class, new MultificationNoticeCdnComposer(resolverRegistry))
            .withMemberResolver(Visibility.PACKAGE_PRIVATE)
            .build();
    }

    public <T> T load(T config, String fileName) {
        this.cdn.load(Source.of(this.dataFolder, fileName), config)
            .orThrow(RuntimeException::new);

        this.cdn.render(config, Source.of(this.dataFolder, fileName))
            .orThrow(RuntimeException::new);

        this.configs.add(new ReloadableConfig(fileName, config));

        return config;
    }

    public void reload() {
        for (ReloadableConfig config : configs) {
            this.cdn.load(Source.of(this.dataFolder, config.fileName()), config.instance())
                .orThrow(RuntimeException::new);
        }
    }

    private record ReloadableConfig(String fileName, Object instance) {
    }

}
