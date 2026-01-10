plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta4"
    id("xyz.jpenilla.run-velocity") version "3.0.2"
}

version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.panda-lang.org/releases/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:${Versions.VELOCITY_API}")
    annotationProcessor("com.velocitypowered:velocity-api:${Versions.VELOCITY_API}")

    implementation("dev.rollczi:litecommands-velocity:3.4.1")
    // implementation("com.eternalcode:multification-velocity:1.2.3") // <-- uncomment in your project
    // implementation("com.eternalcode:multification-cdn:1.2.3") // <-- uncomment in your project

    implementation(project(":multification-velocity")) // don't use this line in your build.gradle
    implementation(project(":multification-cdn")) // don't use this line in your build.gradle
}

val pluginName = "ExampleVelocityPlugin"
val packageName = "com.eternalcode.example.velocity"

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

tasks.runVelocity {
    velocityVersion("1.21.11")
}
