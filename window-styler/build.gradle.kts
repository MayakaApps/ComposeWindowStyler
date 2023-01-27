// Suppress annotation is a workaround for a bug.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)

    alias(libs.plugins.dokka)
    alias(libs.plugins.vanniktech.publish)
}

group = "com.mayakapps.compose"
version = extra["VERSION_NAME"] as String

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }

        withJava()
    }

    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.ui)

                implementation(libs.jna)
                implementation(libs.jna.platform)
            }
        }
    }
}