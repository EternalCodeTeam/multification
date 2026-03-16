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
    implementation("com.google.code.gson:gson:${Versions.GSON}")

    testImplementation("net.kyori:adventure-api:${Versions.ADVENTURE_API}")
}
