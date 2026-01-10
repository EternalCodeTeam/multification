package com.eternalcode.multification.velocity;

import com.eternalcode.multification.Multification;
import com.eternalcode.multification.executor.AsyncExecutor;
import com.eternalcode.multification.locate.LocaleProvider;
import com.eternalcode.multification.viewer.ViewerProvider;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;

public abstract class VelocityMultification<TRANSLATION> extends Multification<CommandSource, TRANSLATION> {

    public static final LocaleProvider<CommandSource> DEFAULT_LOCALE_PROVIDER = new VelocityLocaleProvider();

    private final ProxyServer server;
    private final Plugin plugin;
    private final ViewerProvider<CommandSource> defaultViewerProvider;

    protected VelocityMultification(ProxyServer server, Plugin plugin) {
        super();
        this.server = server;
        this.plugin = plugin;
        this.defaultViewerProvider = new VelocityViewerProvider(server);
    }

    @Override
    protected @NotNull ViewerProvider<CommandSource> viewerProvider() {
        return defaultViewerProvider;
    }

    @Override
    protected @NotNull LocaleProvider<CommandSource> localeProvider() {
        return DEFAULT_LOCALE_PROVIDER;
    }

    @Override
    protected @NotNull AsyncExecutor asyncExecutor() {
        return (runnable) -> server.getScheduler()
                .buildTask(this.plugin, runnable);
    }
}

