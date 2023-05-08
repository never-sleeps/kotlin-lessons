rootProject.name = "kotlin-lessons"
include("L01-simple")

pluginManagement {
    val kotlinVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion apply false
    }
}

include("L01-simple")
include("L02-DSL")
include("L03-coroutines")
include("L04-channels")
