import kotlin.streams.toList

plugins {
    `java-library`
}

group = "net.burningtnt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.getByName<JavaCompile>("compileJava") {
    dependsOn(rootProject.tasks.getByName("compileJava"))

    val bytecodeClasses = listOf(
            "net/burningtnt/bcitest/Implementation"
    )

    doLast {
        javaexec {
            classpath(rootProject.sourceSets["main"].runtimeClasspath)
            mainClass.set("net.burningtnt.bcigenerator.BytecodeImplGenerator")
            System.getProperty("bci.debug.address")?.let { address -> jvmArgs("-agentlib:jdwp=transport=dt_socket,server=n,address=$address,suspend=y") }
            args(bytecodeClasses.stream().map { s -> project.layout.buildDirectory.file("classes/java/main/$s.class").get().asFile.path }.toList())
        }
    }

    outputs.upToDateWhen { false }
}

dependencies {
    compileOnly(rootProject)
}
