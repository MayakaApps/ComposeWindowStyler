package com.mayakapps.compose.windowstyler

import androidx.compose.ui.graphics.Color

sealed interface WindowBackdrop {
    object Default : WindowBackdrop
    data class Solid(override val color: Color) : ColorableWindowBackdrop
    object Transparent : WindowBackdrop
    object Aero : WindowBackdrop
    data class Acrylic(override val color: Color) : ColorableWindowBackdrop
    object Mica : WindowBackdrop
    object Tabbed : WindowBackdrop
}

internal interface ColorableWindowBackdrop : WindowBackdrop {
    val color: Color
}
