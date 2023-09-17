import org.jetbrains.compose.desktop.application.dsl.TargetFormat

// Suppress annotation is a workaround for a bug.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
}

kotlin {
    jvmToolchain(17)

    jvm {
        withJava()
    }

    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(compose.desktop.currentOs)

                implementation(project(":window-styler"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Msi)

            packageName = "window-styler-demo-transparent"
            packageVersion = "1.0.0"
        }
    }
}
