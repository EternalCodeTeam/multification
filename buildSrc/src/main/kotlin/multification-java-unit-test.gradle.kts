plugins {
    id("java-library")
}

dependencies {
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation("org.awaitility:awaitility:4.2.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

sourceSets.test {
    java.setSrcDirs(listOf("test"))
    resources.setSrcDirs(emptyList<String>())
}
