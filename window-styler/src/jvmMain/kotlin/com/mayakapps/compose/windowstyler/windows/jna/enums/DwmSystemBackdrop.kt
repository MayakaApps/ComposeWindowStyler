package com.mayakapps.compose.windowstyler.windows.jna.enums

@Suppress("SpellCheckingInspection", "unused")
internal enum class DwmSystemBackdrop(val value: Int) {
    DWMSBT_AUTO(0),
    DWMSBT_DISABLE(1), // None
    DWMSBT_MAINWINDOW(2), // Mica
    DWMSBT_TRANSIENTWINDOW(3), // Acrylic
    DWMSBT_TABBEDWINDOW(4), // Tabbed
}