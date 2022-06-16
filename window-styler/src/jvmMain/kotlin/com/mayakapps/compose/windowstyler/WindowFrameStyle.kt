package com.mayakapps.compose.windowstyler

import androidx.compose.ui.graphics.Color

data class WindowFrameStyle(
    val borderColor: Color = Color.Unspecified,
    val titleBarColor: Color = Color.Unspecified,
    val captionColor: Color = Color.Unspecified,
    val cornerPreference: WindowCornerPreference = WindowCornerPreference.DEFAULT
)

enum class WindowCornerPreference {
    DEFAULT,
    NOT_ROUNDED,
    ROUNDED,
    SMALL_ROUNDED,
}