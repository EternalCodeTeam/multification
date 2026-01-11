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
    compileOnly("com.velocitypowered:velocity-api:${Versions.VELOCITY_API}")
    testImplementation("com.velocitypowered:velocity-api:${Versions.VELOCITY_API}")
}

