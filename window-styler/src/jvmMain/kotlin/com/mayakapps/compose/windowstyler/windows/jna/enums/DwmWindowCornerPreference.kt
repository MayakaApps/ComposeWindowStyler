package com.mayakapps.compose.windowstyler.windows.jna.enums

@Suppress("SpellCheckingInspection", "unused")
internal enum class DwmWindowCornerPreference(val value: Int) {
    DWMWCP_DEFAULT(0),
    DWMWCP_DONOTROUND(1),
    DWMWCP_ROUND(2),
    DWMWCP_ROUNDSMALL(3),
}