package plugin

import com.android.build.api.dsl.LibraryExtension
import config.composeConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import util.alias
import util.libs

class ComposeLibPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.alias(libs.plugins.android.library)
            extensions.configure<LibraryExtension> { composeConfiguration(this) }
        }
    }
}