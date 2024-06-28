plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.3.0"
}

version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.panda-lang.org/releases/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.stellardrift.ca/repository/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")

    implementation("net.kyori:adventure-platform-bukkit:${Versions.ADVENTURE_PLATFORM_BUKKIT}")
    implementation("net.kyori:adventure-text-minimessage:${Versions.ADVENTURE_API}")
    implementation("dev.rollczi:litecommands-bukkit:3.4.1")
    // implementation("com.eternalcode:multification-bukkit:1.0.3") // <-- uncomment in your project
    // implementation("com.eternalcode:multification-cdn:1.0.3") // <-- uncomment in your project

    implementation(project(":multification-bukkit")) // don't use this line in your build.gradle
    implementation(project(":multification-cdn")) // don't use this line in your build.gradle
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
        "com.eternalcode.multification",
        "net.dzikoysk.cdn",
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
    minecraftVersion("1.20.4")
}
