val minecraftVersion: String by project
val junitVersion: String by project
val neoforgeVersion: String by project
val neoforgeRecipeViewer: String by project
val enableRuntimeRecipeViewer: String by project
val jeiVersion: String by project
val reiVersion: String by project
val emiVersion: String by project

val common by configurations
val shadowCommon by configurations

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

loom {
    // load the test mod for test run configs exclusively
    runs {
        named("test_client") {
            mods {
                create("main") {
                    sourceSet(sourceSets.main.get())
                    sourceSet(project(":Common").sourceSets.main.get())
                }
                create("testmod") {
                    sourceSet(sourceSets.test.get())
                    sourceSet(project(":Common").sourceSets.test.get())
                }
            }
        }
        named("gametest") {
            mods {
                create("main") {
                    sourceSet(sourceSets.main.get())
                    sourceSet(project(":Common").sourceSets.main.get())
                }
                create("testmod") {
                    sourceSet(sourceSets.test.get())
                    sourceSet(project(":Common").sourceSets.test.get())
                }
            }
        }
    }
}

dependencies {
    // loader
    neoForge("net.neoforged:neoforge:${neoforgeVersion}")

    // common module
    common(project(":Common", "namedElements")) { isTransitive = false }
    shadowCommon(project(":Common", "transformProductionNeoForge")) { isTransitive = false }
    testImplementation(project(":Common", "namedElements"))

    // compile time
    modCompileOnly("mezz.jei:jei-$minecraftVersion-neoforge-api:$jeiVersion") { // required for common jei plugin
        isTransitive = false // prevents breaking the forge runtime
    }
    // TODO go back to API when solved: https://github.com/architectury/architectury-loom/issues/204
    modCompileOnly("me.shedaniel:RoughlyEnoughItems-neoforge:$reiVersion") // required for common rei plugin
    modLocalRuntime("dev.architectury:architectury-neoforge:13.0.6") // TODO: Remove on new REI version
    modCompileOnly("dev.emi:emi-neoforge:$emiVersion+1.21:api") // required for common emi plugin TODO: replace on EMI release

    // runtime
    forgeRuntimeLibrary("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    if (enableRuntimeRecipeViewer == "true") {
        when (neoforgeRecipeViewer) {
            "jei" -> modLocalRuntime("mezz.jei:jei-$minecraftVersion-neoforge:$jeiVersion") { isTransitive = false }
            "rei" -> modLocalRuntime("me.shedaniel:RoughlyEnoughItems-neoforge:$reiVersion")
            "emi" -> modLocalRuntime("dev.emi:emi-neoforge:$emiVersion+1.21") //TODO: replace on EMI release
            else -> throw GradleException("Invalid forgeRecipeViewer value: $neoforgeRecipeViewer")
        }
    }
}
