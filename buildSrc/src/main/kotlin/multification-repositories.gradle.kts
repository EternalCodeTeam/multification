plugins {
    `java-library`
}

repositories {
    mavenCentral()

    maven("https://papermc.io/repo/repository/maven-public/") // paper, adventure, velocity
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // spigot
    maven("https://repo.panda-lang.org/releases/") // expressible
    maven("https://repo.stellardrift.ca/repository/snapshots/")
    maven("https://storehouse.okaeri.eu/repository/maven-public/") // okaeri configs
    maven("https://repo.eternalcode.pl/releases") // eternalcode
}