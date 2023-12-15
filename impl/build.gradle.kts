import kotlin.streams.toList

plugins {
    `java-library`
}

group = "net.burningtnt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://libraries.minecraft.net")
}

tasks.compileJava {
    dependsOn(rootProject.tasks.compileJava)

    val bytecodeClasses = listOf(
            "net/burningtnt/bcidemos/"
    )

    doLast {
        javaexec {
            classpath(rootProject.sourceSets["main"].runtimeClasspath)
            mainClass.set("net.burningtnt.bcigenerator.BytecodeImplGenerator")
            System.getProperty("bci.debug.address")?.let { address -> jvmArgs("-agentlib:jdwp=transport=dt_socket,server=n,address=$address,suspend=y") }
            args(bytecodeClasses.stream().map { s -> project.layout.buildDirectory.file("classes/java/main/$s").get().asFile.path }.toList())
        }
    }

    outputs.upToDateWhen { false }
}

dependencies {
    compileOnly(rootProject)

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks.test {
    useJUnitPlatform()
}
