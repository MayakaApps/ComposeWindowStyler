package com.mayakapps.compose.windowstyler.windows.jna.enums

@Suppress("SpellCheckingInspection", "unused")
enum class AccentFlag(val value: Int) {
    NONE(0),
    DRAW_LEFT_BORDER(0x20),
    DRAW_TOP_BORDER(0x40),
    DRAW_RIGHT_BORDER(0x80),
    DRAW_BOTTOM_BORDER(0x100),
    DRAW_ALL_BORDERS(0x1E0), // OR result of all borders
}