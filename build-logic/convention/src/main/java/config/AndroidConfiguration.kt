package config

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion.VERSION_21
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import util.Constants
import util.Constants.freeCompiler


internal fun Project.androidConfiguration(
    applicationExtension: ApplicationExtension
) {
    applicationExtension.apply {
        compileSdk = Constants.MAX_SDK_VERSION
        namespace = Constants.BASE_NAME

        defaultConfig {
            minSdk = Constants.MIN_SDK_VERSION
        }

        buildFeatures {
            buildConfig = true
        }

        compileOptions {
            sourceCompatibility = VERSION_21
            targetCompatibility = VERSION_21
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JVM_21)
            freeCompilerArgs.addAll(freeCompiler)
        }
    }
}

internal fun Project.androidConfiguration(
    libraryExtension: LibraryExtension
) {
    libraryExtension.apply {
        compileSdk = Constants.MAX_SDK_VERSION
        namespace = "${Constants.BASE_NAME}.${project.path.replace(":", ".").substring(1)}"

        defaultConfig {
            minSdk = Constants.MIN_SDK_VERSION
        }

        buildFeatures {
            buildConfig = true
        }

        compileOptions {
            sourceCompatibility = VERSION_21
            targetCompatibility = VERSION_21
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JVM_21)
            freeCompilerArgs.addAll(freeCompiler)
        }
    }
}