import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvmToolchain(17)

    jvm()

    sourceSets {
        jvmMain {
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

            packageName = "window-styler-demo"
            packageVersion = "1.0.0"
        }
    }
}
