package de.lancom.genesis.kotlin

import org.gradle.internal.impldep.org.hamcrest.CoreMatchers.equalTo
import org.gradle.internal.impldep.org.hamcrest.MatcherAssert.assertThat
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test

class SourcesJarTest {

    @Test
    fun `sources jar is created`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("sourcesJar/build.gradle.template")
            }
            file("src/main/kotlin/Main.kt") {
                fromClasspath("sourcesJar/source.kt.template")
            }

            execute(":sourcesJar") {
                assertThat(success, equalTo(true))
                assertThat(build.task(":sourcesJar")?.outcome, equalTo(TaskOutcome.SUCCESS))
                assertThat(fileExists("build/libs/test-project-sources.jar"), equalTo(true))
            }

        }
    }

}