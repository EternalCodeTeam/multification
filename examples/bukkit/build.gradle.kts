plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.3.0"
}

version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.panda-lang.org/releases/") }
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")

    implementation("net.kyori:adventure-platform-bukkit:4.3.0")
    implementation("net.kyori:adventure-text-minimessage:4.14.0")
    implementation("dev.rollczi:litecommands-bukkit:3.4.1")
    // implementation("com.eternalcode:multification-bukkit:1.0.3") // <-- uncomment in your project
    // implementation("com.eternalcode:multification-cdn:1.0.3") // <-- uncomment in your project

    implementation(project(":multification-bukkit")) // don't use this line in your build.gradle
    implementation(project(":multification-cdn")) // don't use this line in your build.gradle
}

val pluginName = "ExamplePlugin"
val packageName = "com.eternalcode.multification.example"

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
    ).forEach { relocate(it, "$packageName.libs.$it") }
}

sourceSets.test {
    java.setSrcDirs(emptyList<String>())
    resources.setSrcDirs(emptyList<String>())
}

tasks.runServer {
    minecraftVersion("1.20.4")
}
