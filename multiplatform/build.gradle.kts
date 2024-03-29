// Multiplatform plugin
plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

// различные target-ы для Multiplatform проекта
kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    // js-1: различные целевые окружения - browser/nodejs
    js(IR) {
        browser {}
    }
    // native-1: выявляем окружение, на котором работаем и по результатам определяем целевую платформу.
    // Выявление целевой платформы и применение interop DSL для конфигурации функционал􏰀ной совместимости Kotlin - C
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosArm64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        compilations.getByName("main") {
            cinterops {
                val libcurl by creating
            }
        }
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }

    val coroutinesVersion: String by project

    // general-3: (Общие сведения) Описание модулей, соответствующих нашим целевым платформам
    //  common - общий код, который мы сможем использовать на разных платформах
    //  для каждой целевой платформы можем указать свои специфические зависимости
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
            }
        }
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        // js-2: зависимости из npm
        val jsMain by getting {
            dependencies {
                implementation(npm("js-big-decimal", "~1.3.4"))
                implementation(npm("is-sorted", "~1.0.5"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val nativeMain by getting
        val nativeTest by getting
    }
}
