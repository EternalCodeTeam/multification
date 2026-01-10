plugins {
    `multification-java`
    `multification-java-17`
    `multification-repositories`
    `multification-publish`
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    api(project(":multification-core"))
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    testImplementation("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}
