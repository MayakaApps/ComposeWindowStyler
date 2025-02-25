// This is necessary to avoid the plugins to be loaded multiple times in each subproject's classloader.
// Suppress annotation is a workaround for a bug.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false

    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.vanniktech.publish) apply false
}

subprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
