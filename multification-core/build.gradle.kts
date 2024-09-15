plugins {
    `multification-java`
    `multification-java-17`
    `multification-java-unit-test`
    `multification-repositories`
    `multification-publish`
}

dependencies {
    compileOnlyApi("net.kyori:adventure-api:${Versions.ADVENTURE_API}")
    api("org.jetbrains:annotations:${Versions.JETBRAINS_ANNOTATIONS}")

    api("com.eternalcode:eternalcode-commons-shared:1.1.3")

    testImplementation("net.kyori:adventure-api:${Versions.ADVENTURE_API}")
}