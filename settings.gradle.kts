rootProject.name = "kotlin-lessons"
include("L01-simple")

pluginManagement {
    val kotlinVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion apply false
    }
}
