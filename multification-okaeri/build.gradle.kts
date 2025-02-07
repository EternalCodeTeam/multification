plugins {
    `multification-java`
    `multification-java-17`
    `multification-java-unit-test`
    `multification-repositories`
    `multification-publish`
}

dependencies {
    api(project(":multification-core"))

    // okaeri configs
    val okaeriConfigsVersion = "5.0.6"
    api("eu.okaeri:okaeri-configs-yaml-snakeyaml:${okaeriConfigsVersion}")
    api("eu.okaeri:okaeri-configs-serdes-commons:${okaeriConfigsVersion}")

    testImplementation(project(":multification-bukkit"))
    testImplementation("org.spigotmc:spigot-api:${Versions.SPIGOT_API}")
    testImplementation("net.kyori:adventure-api:${Versions.ADVENTURE_API}")
}
