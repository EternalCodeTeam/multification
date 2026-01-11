plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta4"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.panda-lang.org/releases/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

    implementation("dev.rollczi:litecommands-bukkit:3.4.1")
    // implementation("com.eternalcode:multification-paper:1.2.3") // <-- uncomment in your project
    // implementation("com.eternalcode:multification-cdn:1.2.3") // <-- uncomment in your project

    implementation(project(":multification-paper")) // don't use this line in your build.gradle
    implementation(project(":multification-cdn")) // don't use this line in your build.gradle
}

val pluginName = "ExamplePaperPlugin"
val packageName = "com.eternalcode.example.paper"

paper {
    main = "$packageName.$pluginName"
    apiVersion = "1.21"
    author = "EternalCode"
    name = pluginName
    version = "${project.version}"
}

tasks.shadowJar {
    archiveFileName.set("$pluginName v${project.version}.jar")

    listOf(
        "dev.rollczi.litecommands",
        "panda.std",
        "panda.utilities",
    ).forEach { relocate(it, "$packageName.libs.$it") }
}

sourceSets.test {
    java.setSrcDirs(emptyList<String>())
    resources.setSrcDirs(emptyList<String>())
}

tasks.runServer {
    minecraftVersion("1.21.11")
}
