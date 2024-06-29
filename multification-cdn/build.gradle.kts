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
    testImplementation("org.spigotmc:spigot-api:${Versions.SPIGOT_API}")
    testImplementation("net.kyori:adventure-api:${Versions.ADVENTURE_API}")
}
