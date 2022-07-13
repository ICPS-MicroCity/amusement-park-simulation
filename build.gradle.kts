/*
 * DEFAULT GRADLE BUILD FOR ALCHEMIST SIMULATOR
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.multiJvmTesting) // Pre-configures the Java toolchains
    alias(libs.plugins.taskTree) // Helps debugging dependencies among gradle tasks
}

repositories {
    mavenCentral()
}

sourceSets {
    main {
        resources {
            srcDir("src/main/protelis")
            srcDir("src/main/yaml")
        }
    }
}

dependencies {
    // Check the catalog at gradle/libs.versions.gradle
    implementation(libs.bundles.alchemist)
    implementation(kotlin("stdlib-jdk8"))
}

multiJvm {
    jvmVersionForCompilation.set(latestJava)
}

val batch: String by project
val maxTime: String by project

val alchemistGroup = "Run Alchemist"
/*
 * This task is used to run all experiments in sequence
 */
val runAll by tasks.register<DefaultTask>("runAll") {
    group = alchemistGroup
    description = "Launches all simulations"
}
/*
 * Scan the folder with the simulation files, and create a task for each one of them.
 */
File(rootProject.rootDir.path + "/src/main/yaml").listFiles()
    .filter { it.extension == "yml" } // pick all yml files in src/main/yaml
    .sortedBy { it.nameWithoutExtension } // sort them, we like reproducibility
    .forEach {
        // one simulation file -> one gradle task
        val task by tasks.register<JavaExec>("run${it.nameWithoutExtension.capitalize()}") {
            group = alchemistGroup // This is for better organization when running ./gradlew tasks
            description = "Launches simulation ${it.nameWithoutExtension}" // Just documentation
            main = "it.unibo.alchemist.Alchemist" // The class to launch
            classpath = sourceSets["main"].runtimeClasspath // The classpath to use
            // In case our simulation produces data, we write it in the following folder:
            val exportsDir = File("${projectDir.path}/build/exports/${it.nameWithoutExtension}")
            doFirst {
                // this is not executed upfront, but only when the task is actually launched
                // If the export folder doesn not exist, create it and its parents if needed
                if (!exportsDir.exists()) {
                    exportsDir.mkdirs()
                }
            }
            // Uses the latest version of java
            javaLauncher.set(
                javaToolchains.launcherFor {
                    languageVersion.set(JavaLanguageVersion.of(multiJvm.latestJava))
                }
            )
            // These are the program arguments
            args("-y", it.absolutePath, "-e", "$exportsDir/${it.nameWithoutExtension}-${System.currentTimeMillis()}")
            if (System.getenv("CI") == "true" || batch == "true") {
                // If it is running in a Continuous Integration environment, use the "headless" mode of the simulator
                // Namely, force the simulator not to use graphical output.
                args("-hl", "-t", maxTime)
            } else {
                // A graphics environment should be available, so load the effects for the UI from the "effects" folder
                // Effects are expected to be named after the simulation file
                args("-g", "effects/${it.nameWithoutExtension}.aes")
            }
            // This tells gradle that this task may modify the content of the export directory
            outputs.dir(exportsDir)
        }
        // task.dependsOn(classpathJar) // Uncomment to switch to jar-based classpath resolution
        runAll.dependsOn(task)
    }

tasks.withType(KotlinCompile::class.java).configureEach {
    kotlinOptions {
        freeCompilerArgs += "-Xjvm-default=enable"
    }
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}