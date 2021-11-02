package de.lancom.genesis.kotlin

import org.gradle.internal.impldep.org.hamcrest.CoreMatchers.equalTo
import org.gradle.internal.impldep.org.hamcrest.MatcherAssert.assertThat
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test

class DokkaTest {

    @Test
    fun `javadoc is generated`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("dokka/build.gradle.template")
            }
            file("src/main/kotlin/Main.kt") {
                fromClasspath("dokka/source.kt.template")
            }

            execute(":dokkaJavadoc") {
                assertThat(success, equalTo(true))
                assertThat(build.task(":dokkaJavadoc")?.outcome, equalTo(TaskOutcome.SUCCESS))
            }
        }
    }

}
