package com.mayakapps.compose.windowstyler

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.connect

// Modified version of toArgb
internal fun Color.toAbgr(): Int {
    val colorSpace = colorSpace
    val color = run { floatArrayOf(red, green, blue, alpha) }

    // The transformation saturates the output
    colorSpace.connect().transform(color)

    return (color[3] * 255.0f + 0.5f).toInt() shl 24 or
            ((color[2] * 255.0f + 0.5f).toInt() shl 16) or
            ((color[1] * 255.0f + 0.5f).toInt() shl 8) or
            (color[0] * 255.0f + 0.5f).toInt()
}