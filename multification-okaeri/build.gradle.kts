plugins {
    `multification-java`
    `multification-java-17`
    `multification-java-unit-test`
    `multification-repositories`
    `multification-publish`
}

dependencies {
    api(project(":multification-core"))

    val okaeriConfigsVersion = "5.0.0-beta.5"
    api("eu.okaeri:okaeri-configs-yaml-snakeyaml:${okaeriConfigsVersion}")
    api("eu.okaeri:okaeri-configs-serdes-commons:${okaeriConfigsVersion}")
}
