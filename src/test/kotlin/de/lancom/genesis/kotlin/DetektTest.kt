package de.lancom.genesis.kotlin

import org.gradle.internal.impldep.org.hamcrest.CoreMatchers.containsString
import org.gradle.internal.impldep.org.hamcrest.CoreMatchers.equalTo
import org.gradle.internal.impldep.org.hamcrest.MatcherAssert.assertThat
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test

class DetektTest {

    @Test
    fun `generate default configuration succeeds`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("detekt/build.gradle.template")
            }

            execute(":detektGenerateConfig") {
                assertThat(success, equalTo(true))
                assertThat(build.task(":detektGenerateConfig")?.outcome, equalTo(TaskOutcome.SUCCESS))
                assertThat(fileExists("config/detekt/detekt.yml"), equalTo(true))
            }
        }
    }

    @Test
    fun `check with default validation fails on invalid file`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("detekt/build.gradle.template")
            }
            file("src/main/kotlin/Main.kt") {
                fromClasspath("detekt/source-fail.kt.template")
            }

            execute(":detektMain") {
                assertThat(success, equalTo(false))
                assertThat(build.task(":detektMain")?.outcome, equalTo(TaskOutcome.FAILED))
                assertThat(build.output, containsString("FinalNewline"))
                assertThat(build.output, containsString("NoUnusedImports"))
            }
        }
    }

    @Test
    fun `check with default validation succeeds on valid file`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("detekt/build.gradle.template")
            }
            file("src/main/kotlin/Main.kt") {
                fromClasspath("detekt/source-success.kt.template")
            }

            execute(":detektMain") {
                assertThat(success, equalTo(true))
                assertThat(build.task(":detektMain")?.outcome, equalTo(TaskOutcome.SUCCESS))
            }
        }
    }

    @Test
    fun `check with custom validation succeeds on invalid file`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("detekt/build.gradle.template")
            }
            file("src/main/kotlin/Main.kt") {
                fromClasspath("detekt/source-fail.kt.template")
            }
            file("config/detekt/detekt.yml") {
                fromClasspath("detekt/detekt.yml.template")
            }

            execute(":detektMain") {
                assertThat(success, equalTo(true))
                assertThat(build.task(":detektMain")?.outcome, equalTo(TaskOutcome.SUCCESS))
            }
        }
    }

}