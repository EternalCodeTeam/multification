plugins {
    `multification-java`
    `multification-java-17`
    `multification-repositories`
    `multification-publish`
}

dependencies {
    api(project(":multification-core"))

    compileOnly("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
    testImplementation("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
}