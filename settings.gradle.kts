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

include("L01-simple")
include("L02-DSL")
include("L03-coroutines")
include("L04-channels")
include("L05-multiplatform")
include("L05-validation")
