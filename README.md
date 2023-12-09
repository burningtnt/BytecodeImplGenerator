# BCIG: Bytecode Implementation Generator

Minimal library for generating bytecode implemented methods.

## Knowledge Required
Please make sure that you have understood Java Bytecode and learned ASM.

## Usage

#### Gradle
Declare this maven.
```kotlin
maven(url = "https://libraries.minecraft.net")
```

Import Bytecode Implementation.

Add the following codes in your `build.gradle.kts` configuration file.
```kotlin
tasks.compileJava {
    val bytecodeClasses = listOf(
            "org/example/project/YourClassName"
    )
    val sourceSet = "main"

    doLast {
        javaexec {
            classpath(rootProject.sourceSets[sourceSet].runtimeClasspath)
            mainClass.set("net.burningtnt.bcigenerator.BytecodeImplGenerator")
            args(bytecodeClasses.stream().map { s -> project.layout.buildDirectory.file("classes/java/$sourceSet/$s.class").get().asFile.path }.toList())
        }
    }
}
```
Here, the `bytecodeClasses` are the white list of classes that Bytecode Implementation Generator should process.
The classes that is not declared here would NOT be processed by Bytecode Implementation Generator.

# Grammar Reference
[Here](Grammar.md) is the full grammar reference.

[Here](HelloBCIG.md) is the guild for you to make your first bytecode implemented method.
