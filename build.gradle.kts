import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "de.lancom.genesis"
version = "1.0.0"

val kotlinVersion = "1.4.21"

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.21"
    id("maven-publish")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.12.0"
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
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:0.10.1")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.15.0")
    implementation("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-noarg:$kotlinVersion")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:9.4.1")

    testImplementation(gradleTestKit())
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
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
