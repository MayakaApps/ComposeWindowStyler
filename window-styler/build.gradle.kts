// Suppress annotation is a workaround for a bug.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)

    alias(libs.plugins.dokka)
    alias(libs.plugins.vanniktech.publish)
}

group = extra["GROUP"] as String
version = extra["VERSION_NAME"] as String

kotlin {
    jvmToolchain(17)
    jvm {
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