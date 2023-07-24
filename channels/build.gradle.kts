plugins {
    kotlin("jvm")
}

val coroutinesVersion: String by project
val jUnitJupiterVersion: String by project

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    // implementation вместо testImplementation используется сознательно, поскольку в учебных целях тесты вынесены в основной код
    implementation(kotlin("test-junit"))
    implementation("org.junit.jupiter:junit-jupiter-api:$jUnitJupiterVersion")
    implementation("org.junit.jupiter:junit-jupiter-engine:$jUnitJupiterVersion")
}

tasks.test {
    useJUnitPlatform()
}
