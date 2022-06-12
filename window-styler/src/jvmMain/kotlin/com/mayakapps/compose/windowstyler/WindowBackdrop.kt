package com.mayakapps.compose.windowstyler

sealed interface WindowBackdrop {
    object Default : WindowBackdrop
    object Transparent : WindowBackdrop
    data class Solid(val color: Int) : WindowBackdrop
    object Aero : WindowBackdrop
    data class Acrylic(val color: Int) : WindowBackdrop
    data class Mica(val isDark: Boolean) : WindowBackdrop
    object Tabbed : WindowBackdrop
}
