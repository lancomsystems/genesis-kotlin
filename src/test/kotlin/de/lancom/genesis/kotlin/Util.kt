package de.lancom.genesis.kotlin

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.UnexpectedBuildFailure
import java.io.File

/**
 * Provides an api for the creation of files in the test project.
 */
interface TestFile {
    fun fromClasspath(path: String)
    fun fromText(text: String)
}

/**
 * Provides an api to setup a GradleRunner test.
 */
interface TestSetup {

    /**
     * Root directory of the test project.
     */
    val root: File

    /**
     * Enable debug support for the test.
     * Might lead to classpath problems.
     */
    var debug: Boolean

    fun file(path: String, block: TestFile.() -> Unit)

    fun execute(vararg arguments: String, result: TestResult.() -> Unit)

    fun fileExists(path: String) = root.resolve(path).isFile

    fun directoryExists(path: String) = root.resolve(path).isDirectory
}

/**
 * Provides information about the test result.
 */
interface TestResult {
    val success: Boolean
    val build: BuildResult
}

/**
 *  Utility function that allows easy setup, execution and validation of projects for unit tests using gradle runner.
 *  @see GradleRunner
 */
inline fun testProject(block: TestSetup.() -> Unit) {
    val root = File.createTempFile("pluginTest", "").apply {
        delete()
        mkdirs()
    }

    val classLoader = Thread.currentThread().contextClassLoader

    try {
        block(object : TestSetup {
            override val root: File get() = root
            override var debug: Boolean = false

            override fun file(path: String, block: TestFile.() -> Unit) {
                val file = root.resolve(path).apply {
                    parentFile.mkdirs()
                }
                block(object : TestFile {
                    override fun fromClasspath(path: String) {
                        val resource = checkNotNull(classLoader.getResource(path)) {
                            "Could not find $path"
                        }
                        file.writeBytes(resource.readBytes())
                    }

                    override fun fromText(text: String) {
                        file.writeText(text.trimIndent())
                    }
                })
            }

            override fun execute(vararg arguments: String, result: TestResult.() -> Unit) {
                result(try {
                    object : TestResult {
                        override val success = true
                        override val build = GradleRunner.create().run {
                            withProjectDir(root)
                            withArguments(*arguments)
                            withPluginClasspath()
                            withDebug(debug)
                            forwardOutput()
                            build()
                        }
                    }
                } catch (ex: UnexpectedBuildFailure) {
                    object : TestResult {
                        override val success = false
                        override val build = ex.buildResult
                    }
                })
            }

        })
    } finally {
        root.deleteRecursively()
    }

}