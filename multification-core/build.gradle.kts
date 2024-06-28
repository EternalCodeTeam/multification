plugins {
    `multification-java`
    `multification-java-17`
    `multification-java-unit-test`
    `multification-repositories`
    `multification-publish`
}

dependencies {
    compileOnlyApi("net.kyori:adventure-api:4.17.0")
    api("org.jetbrains:annotations:24.1.0")
}