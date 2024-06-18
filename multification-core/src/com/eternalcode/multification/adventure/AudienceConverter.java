package com.eternalcode.multification.adventure;

import net.kyori.adventure.audience.Audience;

public interface AudienceConverter<VIEWER> {

    Audience convert(VIEWER viewer);

}
