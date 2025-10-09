plugins {
    `multification-java`
    `multification-java-17`
    `multification-repositories`
    `multification-publish`
}

dependencies {
    api(project(":multification-core"))

    compileOnly("org.spigotmc:spigot-api:${Versions.SPIGOT_API}")
    testImplementation("org.spigotmc:spigot-api:${Versions.SPIGOT_API}")

    compileOnly("com.github.retrooper:packetevents-spigot:${Versions.PACKETEVENTS}")
    testImplementation("com.github.retrooper:packetevents-spigot:${Versions.PACKETEVENTS}")
}