plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    alias(libs.plugins.dokka)
    alias(libs.plugins.vanniktech.publish)
}

group = extra["GROUP"] as String
version = extra["VERSION_NAME"] as String

kotlin {
    jvmToolchain(17)

    jvm()

    sourceSets {
        jvmMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.ui)

                implementation(libs.jna)
                implementation(libs.jna.platform)
            }
        }
    }
}
