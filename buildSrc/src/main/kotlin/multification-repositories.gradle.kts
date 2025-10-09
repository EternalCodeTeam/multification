plugins {
    `java-library`
}

repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // spigot
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") // adventure snapshots
    maven("https://storehouse.okaeri.eu/repository/maven-public/") // okaeri configs
    maven("https://repo.stellardrift.ca/repository/snapshots/") // ? xd
    maven("https://repo.codemc.io/repository/maven-releases/") // packetevents
    maven("https://papermc.io/repo/repository/maven-public/") // paper, adventure, velocity
    maven("https://repo.panda-lang.org/releases/") // expressible
}