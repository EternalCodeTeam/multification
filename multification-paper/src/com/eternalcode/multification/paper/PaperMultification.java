package com.eternalcode.multification.paper;

import com.eternalcode.multification.Multification;
import com.eternalcode.multification.executor.AsyncExecutor;
import com.eternalcode.multification.locate.LocaleProvider;
import com.eternalcode.multification.viewer.ViewerProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class PaperMultification<TRANSLATION> extends Multification<CommandSender, TRANSLATION> {

    public static final ViewerProvider<CommandSender> DEFAULT_VIEWER_PROVIDER = new PaperViewerProvider();
    public static final LocaleProvider<CommandSender> DEFAULT_LOCALE_PROVIDER = new PaperLocaleProvider();

    protected PaperMultification() {
        super();
    }

    @Override
    protected @NotNull ViewerProvider<CommandSender> viewerProvider() {
        return DEFAULT_VIEWER_PROVIDER;
    }

    @Override
    protected @NotNull LocaleProvider<CommandSender> localeProvider() {
        return DEFAULT_LOCALE_PROVIDER;
    }

    @Override
    protected @NotNull AsyncExecutor asyncExecutor() {
        return (runnable) -> Bukkit.getScheduler()
                .runTaskAsynchronously(JavaPlugin.getProvidingPlugin(PaperMultification.class), runnable);
    }
}
