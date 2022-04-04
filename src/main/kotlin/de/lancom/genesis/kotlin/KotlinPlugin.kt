package de.lancom.genesis.kotlin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper

@Suppress("unused")
/**
 * Genesis Kotlin plugin.
 */
class KotlinPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply(KotlinPluginWrapper::class.java)

        val extension = project.extensions.create("genesisKotlin", KotlinExtension::class.java, project)

        project.tasks.withType(KotlinJvmCompile::class.java).configureEach { task ->
            task.kotlinOptions.jvmTarget = extension.jvmVersion.get().toString()
            task.kotlinOptions.freeCompilerArgs = extension.freeCompilerArgs.get()
        }
    }
}
