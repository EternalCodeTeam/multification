plugins {
    `multification-java`
    `multification-java-17`
    `multification-java-unit-test`
    `multification-repositories`
    `multification-publish`
}

dependencies {
    api(project(":multification-core"))

    api("net.dzikoysk:cdn:1.14.4")
    api("dev.rollczi:litecommands-core:3.2.2")
}
