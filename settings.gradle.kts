// Copyright 2000-2022 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        kotlin("multiplatform") version (extra["kotlin.version"] as String)
        id("org.jetbrains.compose") version (extra["compose.version"] as String)

        id("org.jetbrains.dokka") version "1.7.0"
        id("com.vanniktech.maven.publish") version "0.20.0"
    }
}

rootProject.name = "ComposeWindowStyler"
include("window-styler", "window-styler-demo")