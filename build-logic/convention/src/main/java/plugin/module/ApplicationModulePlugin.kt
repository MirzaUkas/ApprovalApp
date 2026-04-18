package plugin.module

import com.android.build.api.dsl.ApplicationExtension
import config.androidConfiguration
import config.composeConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import util.Constants.MAX_SDK_VERSION
import util.Constants.resourceExcludes
import util.alias
import util.libs

class ApplicationModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.plugins.android.application)
                alias(libs.plugins.kotlin.compose)
                alias(libs.plugins.convention.hilt)
            }

            extensions.configure<ApplicationExtension> {
                androidConfiguration(this)
                composeConfiguration(this)
                defaultConfig.targetSdk = MAX_SDK_VERSION
                packaging.resources.excludes.addAll(resourceExcludes)
            }
        }
    }
}