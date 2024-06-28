package dev.rollczi.example.bukkit.config;

import com.eternalcode.multification.cdn.MultificationNoticeCdnComposer;
import com.eternalcode.multification.notice.Notice;
import com.eternalcode.multification.notice.resolver.NoticeResolverRegistry;
import java.io.File;
import net.dzikoysk.cdn.Cdn;
import net.dzikoysk.cdn.CdnFactory;
import net.dzikoysk.cdn.reflect.Visibility;
import net.dzikoysk.cdn.source.Source;
public class ConfigurationManager {

    private final Cdn cdn;
    private final File dataFolder;

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

        return config;
    }

}
