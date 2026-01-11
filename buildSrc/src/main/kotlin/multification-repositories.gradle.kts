plugins {
    `java-library`
}

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/") // paper, adventure, velocity
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // spigot
    maven("https://repo.panda-lang.org/releases/") // expressible
    maven("https://repo.stellardrift.ca/repository/snapshots/")
    maven("https://repo.okaeri.cloud/releases") // okaeri configs
    maven("https://repo.okaeri.cloud/snapshots")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") // adventure snapshots
}
