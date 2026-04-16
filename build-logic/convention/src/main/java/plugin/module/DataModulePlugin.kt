package plugin.module

import org.gradle.api.Plugin
import org.gradle.api.Project
import util.alias
import util.dataDependencies
import util.libs

class DataModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.plugins.convention.android.library)
            }
            dataDependencies()
        }
    }
}