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

dokka {
    moduleName.set("Compose Window Styler")

    dokkaPublications.html {
        outputDirectory.set(rootProject.layout.projectDirectory.dir("docs/api"))
    }

    pluginsConfiguration.html {
        customStyleSheets.from(rootProject.layout.projectDirectory.file("docs/styles/dokka.css"))
        customAssets.from(rootProject.layout.projectDirectory.file("docs/images/logo.png"))

        footerMessage.set("Copyright &copy; 2023-2025 MayakaApps.")
    }
}
