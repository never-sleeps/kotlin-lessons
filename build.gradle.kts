plugins {
    kotlin("jvm")
}

group = "me.neversleeps"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

subprojects {
    group = "me.neversleeps"
    version = "1.0-SNAPSHOT"

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile> {
        kotlinOptions.jvmTarget = "17"
    }
}
