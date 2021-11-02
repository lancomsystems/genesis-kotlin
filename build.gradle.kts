import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "de.lancom.genesis"
version = "1.1.0"

val kotlinVersion = "1.5.31"

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.31"
    id("maven-publish")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.16.0"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.5.31")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.18.1")
    implementation("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-noarg:$kotlinVersion")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:10.2.0")

    testImplementation(gradleTestKit())
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

gradlePlugin {
    plugins {
        register("kotlin") {
            id = "de.lancom.genesis.kotlin"
            displayName = "Lancom Genesis Kotlin Plugin"
            description = "Plugin for basic configuration of Kotlin based Gradle projects"
            implementationClass = "de.lancom.genesis.kotlin.KotlinPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/lancomsystems/genesis-kotlin"
    vcsUrl = "https://github.com/lancomsystems/genesis-kotlin.git"
    tags = listOf("kotlin")
}

tasks.withType(Test::class.java) {
    useJUnitPlatform()
    testLogging {
        events = setOf(PASSED, FAILED)
    }
}

tasks.withType(KotlinCompile::class.java).all {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType(JavaCompile::class.java).all {
    targetCompatibility = "1.8"
}
