package de.lancom.genesis.kotlin

import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.javadoc.Javadoc
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.allopen.gradle.SpringGradleSubplugin
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.noarg.gradle.KotlinJpaSubplugin
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

open class KotlinExtension(
    private val project: Project
) {
    private val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)

    val jvmVersion = project.objects.property(JavaVersion::class.java).convention(JavaVersion.VERSION_1_8)

    fun withJavadocJar() {
        javaExtension.withJavadocJar()
        withDokka()
    }

    fun withSourcesJar() {
        javaExtension.withSourcesJar()
    }

    fun withDokka() {
        project.pluginManager.apply(DokkaPlugin::class.java)
    }

    /**
     * Pull in kotlin-spring.
     *
     * Shorthand for
     *   id "org.jetbrains.kotlin.plugin.spring"
     *
     * See https://kotlinlang.org/docs/reference/compiler-plugins.html
     */
    fun withSpringSupport() {
        project.plugins.apply(SpringGradleSubplugin::class.java)
    }

    /**
     * Pull in kotlin-jpa
     *
     * Shorthand for
     *  id "org.jetbrains.kotlin.plugin.jpa"
     */
    fun withJpaSupport() {
        project.plugins.apply(KotlinJpaSubplugin::class.java)
    }

    @JvmOverloads
    fun withKtlint(
        ktlintVersion: String = "0.43.0"
    ) {
        project.plugins.apply(KtlintPlugin::class.java)
        project.extensions.configure(KtlintExtension::class.java) {
            it.version.set(ktlintVersion)

            /** Force the build to fail on lint issues **/
            it.reporters {
                it.reporter(ReporterType.PLAIN)
            }
        }
    }

    @JvmOverloads
    fun withDetekt(
        detektVersion: String = "1.18.1",
    ) {
        project.plugins.apply(DetektPlugin::class.java)

        project.extensions.configure(DetektExtension::class.java) {
            it.toolVersion = detektVersion
            it.parallel = true
            it.buildUponDefaultConfig = true
            it.reports.html.enabled = true
            it.reports.txt.enabled = false
            it.reports.xml.enabled = false
        }

        project.dependencies.add("detektPlugins", "io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")
    }

}
