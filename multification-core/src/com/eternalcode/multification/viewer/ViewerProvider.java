package com.eternalcode.multification.viewer;

import java.util.Collection;
import java.util.UUID;

public interface ViewerProvider<Viewer> {

    Viewer console();

    Viewer player(UUID uuid);

    Collection<Viewer> onlinePlayers();

    Collection<Viewer> all();

}
