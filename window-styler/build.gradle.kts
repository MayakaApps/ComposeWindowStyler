import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")

    id("maven-publish")
    id("com.vanniktech.maven.publish")
}

group = "com.mayakapps.compose"
version = "0.1.0"

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

                implementation("net.java.dev.jna:jna:5.11.0")
                implementation("net.java.dev.jna:jna-platform:5.11.0")
            }
        }
    }
}

configure<PublishingExtension> {
    repositories {
        maven {
            name = "GitHubPackages"
            setUrl("https://maven.pkg.github.com/MayakaApps/ComposeWindowStyler")

            credentials {
                username = project.properties["GITHUB_USER"] as String?
                    ?: System.getenv("GITHUB_ACTOR")

                password = project.properties["GITHUB_PUBLISHING_TOKEN"] as String?
                    ?: System.getenv("GITHUB_PUBLISHING_TOKEN")
            }
        }
    }
}
