package de.lancom.genesis.kotlin

import org.gradle.internal.impldep.org.hamcrest.CoreMatchers.containsString
import org.gradle.internal.impldep.org.hamcrest.CoreMatchers.equalTo
import org.gradle.internal.impldep.org.hamcrest.MatcherAssert.assertThat
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test

class KtlintTest {

    @Test
    fun `check with default validation fails on invalid file`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("ktlint/build.gradle.template")
            }
            file("src/main/kotlin/Main.kt") {
                fromClasspath("ktlint/source-fail.kt.template")
            }
            execute(":ktlintCheck") {
                assertThat(success, equalTo(false))
                assertThat(build.task(":ktlintMainSourceSetCheck")?.outcome, equalTo(TaskOutcome.FAILED))
                assertThat(build.output, containsString("File must end with a newline"))
                assertThat(build.output, containsString("Unused import"))
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
                fromClasspath("ktlint/build.gradle.template")
            }
            file("src/main/kotlin/Main.kt") {
                fromClasspath("ktlint/source-success.kt.template")
            }
            execute(":ktlintCheck") {
                assertThat(success, equalTo(true))
                assertThat(build.task(":ktlintMainSourceSetCheck")?.outcome, equalTo(TaskOutcome.SUCCESS))
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
                fromClasspath("ktlint/build.gradle.template")
            }
            file("src/main/kotlin/Main.kt") {
                fromClasspath("ktlint/source-fail.kt.template")
            }
            file(".editorconfig") {
                fromClasspath("ktlint/editorconfig.template")
            }
            execute(":ktlintCheck") {
                assertThat(success, equalTo(true))
                assertThat(build.task(":ktlintMainSourceSetCheck")?.outcome, equalTo(TaskOutcome.SUCCESS))
            }
        }
    }


    @Test
    fun `check with custom validation succeeds on invalid file after format`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("ktlint/build.gradle.template")
            }
            file("src/main/kotlin/Main.kt") {
                fromClasspath("ktlint/source-fail.kt.template")
            }
            execute(":ktlintFormat") {
                assertThat(success, equalTo(true))
            }
            execute(":ktlintCheck") {
                assertThat(success, equalTo(true))
                assertThat(build.task(":ktlintMainSourceSetCheck")?.outcome, equalTo(TaskOutcome.SUCCESS))
            }
        }
    }

}