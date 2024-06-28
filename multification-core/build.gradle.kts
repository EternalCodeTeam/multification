plugins {
    `multification-java`
    `multification-java-17`
    `multification-java-unit-test`
    `multification-repositories`
    `multification-publish`
}

dependencies {
    compileOnlyApi("net.kyori:adventure-api:${Versions.ADVENTURE_API}")
    api("org.jetbrains:annotations:24.1.0")

    testImplementation("net.kyori:adventure-api:${Versions.ADVENTURE_API}")
}