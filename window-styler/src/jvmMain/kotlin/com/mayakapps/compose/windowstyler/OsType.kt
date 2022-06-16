package com.mayakapps.compose.windowstyler

internal enum class OsType {
    UNKNOWN,
    WINDOWS,
    LINUX,
    MACOS,
    ;

    companion object {
        val current by lazy {
            val osName = System.getProperty("os.name") ?: return@lazy UNKNOWN
            when {
                osName.startsWith("Windows", ignoreCase = true) -> WINDOWS
                osName.startsWith("Linux", ignoreCase = true) -> LINUX
                osName.startsWith("Mac", ignoreCase = true) -> MACOS
                else -> UNKNOWN
            }
        }
    }
}