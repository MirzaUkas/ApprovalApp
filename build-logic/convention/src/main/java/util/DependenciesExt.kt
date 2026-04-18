package util

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import util.Constants.coreModules

fun Project.composeDependencies() {
    dependencies {
        val bom = libs.androidx.compose.bom.get()
        implementation(platform(bom))
        androidTestImplementation(platform(bom))
        implementation(libs.androidx.activity.compose.get())
        implementation(libs.androidx.appcompat.get())
        implementation(libs.androidx.compose.material3)
        implementation(libs.androidx.compose.material3.adaptive.navigation.suite.get())
        implementation(libs.androidx.compose.material.icons.core.get())
        implementation(libs.androidx.compose.ui.tooling.preview.get())
        implementation(libs.androidx.core.ktx.get())
        implementation(libs.timber.get())
    }
}

fun Project.dataDependencies() {
    dependencies {
        implementation(project(coreModules[1]))
        implementation(libs.okhttp.interceptor.get())
        implementation(libs.retrofit.lib.get())
        implementation(libs.retrofit.converter.get())
        implementation(libs.kotlinx.serialization.json.get())
        implementation(libs.timber.get())
    }
}