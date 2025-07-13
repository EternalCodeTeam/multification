package com.eternalcode.multification.viewer;

import java.util.Collection;
import java.util.UUID;

public interface ViewerProvider<VIEWER> {

    VIEWER console();

    VIEWER player(UUID uuid);

    Collection<VIEWER> onlinePlayers();

    Collection<VIEWER> onlinePlayers(String permission);

    Collection<VIEWER> all();

}
