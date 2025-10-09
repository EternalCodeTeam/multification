plugins {
    `multification-java`
    `multification-java-17`
    `multification-java-unit-test`
    `multification-repositories`
    `multification-publish`
}

dependencies {
    compileOnlyApi("net.kyori:adventure-api:${Versions.ADVENTURE_API}")
    compileOnlyApi("net.kyori:adventure-platform-bukkit:${Versions.ADVENTURE_PLATFORM_BUKKIT}")
    api("org.jetbrains:annotations:${Versions.JETBRAINS_ANNOTATIONS}")

    testImplementation("net.kyori:adventure-api:${Versions.ADVENTURE_API}")
}