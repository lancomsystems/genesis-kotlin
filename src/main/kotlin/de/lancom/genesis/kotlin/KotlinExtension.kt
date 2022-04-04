package de.lancom.genesis.kotlin

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.ListProperty
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.kotlin.allopen.gradle.SpringGradleSubplugin
import org.jetbrains.kotlin.noarg.gradle.KotlinJpaSubplugin
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

fun Project.configureGenesisKotlin() {
    extensions.findByType(KotlinExtension::class.java)?.run {
        // enable additional kotlin compiler plugins
        withSpringSupport() // org.jetbrains.kotlin.plugin.spring
        withJpaSupport() // org.jetbrains.kotlin.plugin.jpa
        withKtlint()
    }
}

open class KotlinExtension(
    private val project: Project
) {
    private val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)

    val jvmVersion = project.objects.property(JavaVersion::class.java).convention(JavaVersion.VERSION_1_8)

    val freeCompilerArgs: ListProperty<String> = project.objects.listProperty(String::class.java)
        .convention(listOf("-Xjsr305=strict", "-Xjvm-default=all"))

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
        }

        project.tasks.withType(Detekt::class.java) { detekt ->
            detekt.reports.run {
                html.required.set(true)
                txt.required.set(false)
                xml.required.set(false)
            }
        }

        project.dependencies.add("detektPlugins", "io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")
    }

}
