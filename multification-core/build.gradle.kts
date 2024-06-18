plugins {
    `multification-java`
    `multification-java-17`
    `multification-java-unit-test`
    `multification-repositories`
    `multification-publish`
}

dependencies {
    api("net.kyori:adventure-api:4.17.0")
    api("org.jetbrains:annotations:24.1.0")

    // TODO: Remove Spigot API, and use kyori in core. for Bukkit, create bukkit-platform.
    compileOnly("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
    testImplementation("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
}