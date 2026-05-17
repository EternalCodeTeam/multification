plugins {
    `multification-java`
    `multification-java-21`
    `multification-repositories`
    `multification-publish`
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    api(project(":multification-core"))
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.64-stable")
    testImplementation("io.papermc.paper:paper-api:26.1.2.build.64-stable")
}
