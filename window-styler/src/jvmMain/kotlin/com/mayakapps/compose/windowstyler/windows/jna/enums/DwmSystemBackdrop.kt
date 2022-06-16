package com.mayakapps.compose.windowstyler.windows.jna.enums

import com.sun.jna.ptr.IntByReference

@Suppress("SpellCheckingInspection", "unused")
internal enum class DwmSystemBackdrop(private val value: Int) {
    DWMSBT_AUTO(0),
    DWMSBT_DISABLE(1), // None
    DWMSBT_MAINWINDOW(2), // Mica
    DWMSBT_TRANSIENTWINDOW(3), // Acrylic
    DWMSBT_TABBEDWINDOW(4), // Tabbed
    ;

    val intByReference by lazy { IntByReference(value) }
}