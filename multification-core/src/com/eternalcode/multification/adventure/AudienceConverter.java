package com.eternalcode.multification.adventure;

import net.kyori.adventure.audience.Audience;

public interface AudienceConverter<Viewer> {

    Audience convert(Viewer viewer);

}
