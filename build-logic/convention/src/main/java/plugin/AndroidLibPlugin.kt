package plugin

import com.android.build.api.dsl.LibraryExtension
import config.androidConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import util.Constants
import util.Constants.MAX_SDK_VERSION
import util.alias
import util.libs

class AndroidLibPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.plugins.android.library)
                alias(libs.plugins.kotlin.compose)
                alias(libs.plugins.convention.hilt)
            }

            extensions.configure<LibraryExtension> {
                androidConfiguration(this)
                defaultConfig.minSdk = Constants.MIN_SDK_VERSION
            }
        }
    }
}