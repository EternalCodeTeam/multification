plugins {
    `multification-java`
    `multification-java-17`
    `multification-java-unit-test`
    `multification-repositories`
    `multification-publish`
}

dependencies {
    api(project(":multification-core"))

    api("net.dzikoysk:cdn:${Versions.CDN}")

    testImplementation(project(":multification-bukkit"))
    testImplementation("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
    testImplementation("net.kyori:adventure-api:${Versions.ADVENTURE_API}")
}
