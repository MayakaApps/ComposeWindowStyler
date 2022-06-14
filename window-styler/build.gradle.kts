import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")

    id("maven-publish")
    id("com.vanniktech.maven.publish")
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

                implementation("net.java.dev.jna:jna:5.11.0")
                implementation("net.java.dev.jna:jna-platform:5.11.0")
            }
        }
    }
}