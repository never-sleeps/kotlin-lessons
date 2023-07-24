rootProject.name = "kotlin-lessons"
include("L01-simple")

pluginManagement {
    val kotlinVersion: String by settings
    val springframeworkBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val pluginSpringVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion apply false

        id("org.springframework.boot") version springframeworkBootVersion apply false
        id("io.spring.dependency-management") version springDependencyManagementVersion apply false
        kotlin("plugin.spring") version pluginSpringVersion apply false
    }
}

include("simple")
include("DSL")
include("coroutines")
include("channels")
include("multiplatform")
include("validation")
