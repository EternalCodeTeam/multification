plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta4"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.panda-lang.org/releases/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.codemc.io/repository/maven-releases/") // packetevents
    maven("https://repo.stellardrift.ca/repository/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")

    implementation("net.kyori:adventure-platform-bukkit:${Versions.ADVENTURE_PLATFORM_BUKKIT}")
    implementation("net.kyori:adventure-text-minimessage:${Versions.ADVENTURE_API}")
    implementation("dev.rollczi:litecommands-bukkit:3.4.1")
    // implementation("com.eternalcode:multification-bukkit:1.0.3") // <-- uncomment in your project
    // implementation("com.eternalcode:multification-cdn:1.0.3") // <-- uncomment in your project

    implementation(project(":multification-bukkit"))
    implementation(project(":multification-cdn"))

    compileOnly("com.github.retrooper:packetevents-spigot:${Versions.PACKETEVENTS}")
}

val pluginName = "ExamplePlugin"
val packageName = "com.eternalcode.example.bukkit"

bukkit {
    main = "$packageName.$pluginName"
    apiVersion = "1.13"
    author = "Rollczi"
    name = pluginName
    version = "${project.version}"
}

tasks.shadowJar {
    archiveFileName.set("$pluginName v${project.version}.jar")

    listOf(
//        "com.eternalcode.multification",
//        "net.dzikoysk.cdn",
        "panda.std",
        "panda.utilities",
        "net.kyori",
    ).forEach { relocate(it, "$packageName.libs.$it") }
}

sourceSets.test {
    java.setSrcDirs(emptyList<String>())
    resources.setSrcDirs(emptyList<String>())
}

tasks.runServer {
    minecraftVersion("1.21.8")

    downloadPlugins {
        downloadPlugins.url("https://cdn.modrinth.com/data/HYKaKraK/versions/Kee6pozk/packetevents-spigot-2.9.5.jar")
    }
}
