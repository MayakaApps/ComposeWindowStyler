import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")

    id("maven-publish")
    id("com.vanniktech.maven.publish")
}

group = "com.mayakapps.composebackdrop"
version = "0.1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)

                implementation("net.java.dev.jna:jna:5.11.0")
                implementation("net.java.dev.jna:jna-platform:5.11.0")
            }
        }
        val jvmTest by getting
    }
}

configure<PublishingExtension> {
    repositories {
        maven {
            name = "GitHubPackages"
            setUrl("https://maven.pkg.github.com/MayakaApps/ComposeBackdrop")
            credentials {
                username = project.properties["GITHUB_USER"] as String?
                    ?: System.getenv("GITHUB_ACTOR")

                password = project.properties["GITHUB_PUBLISHING_TOKEN"] as String?
                    ?: System.getenv("GITHUB_PUBLISHING_TOKEN")
            }
        }
    }
}