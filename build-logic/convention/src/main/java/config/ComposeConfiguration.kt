package config

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import util.composeDependencies

internal fun Project.composeConfiguration(
    applicationExtension: ApplicationExtension,
) = applicationExtension.apply {
    buildFeatures { compose = true }
    composeDependencies()
}

internal fun Project.composeConfiguration(
   libraryExtension: LibraryExtension,
) = libraryExtension.apply {
    buildFeatures { compose = true }
    composeDependencies()
}