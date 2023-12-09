plugins {
    `java-library`
    `maven-publish`
    checkstyle
}

group = "net.burningtnt"
version = "1.0-SNAPSHOT"
description = "Minimal library for generating bytecode implemented methods."

repositories {
    mavenCentral()
    maven(url = "https://libraries.minecraft.net")
}

dependencies {
    implementation("org.ow2.asm:asm:9.6")
    implementation("com.mojang:brigadier:1.0.18")
}

tasks.build {
    dependsOn(tasks.checkstyleMain)
}

tasks.checkstyleMain {
    group = "build"
}

tasks.compileJava {
    options.release.set(8)

    sourceCompatibility = "8"
    targetCompatibility = "8"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])
        }
    }
}