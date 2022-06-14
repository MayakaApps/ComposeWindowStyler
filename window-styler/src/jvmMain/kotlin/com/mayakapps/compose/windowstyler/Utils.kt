package com.mayakapps.compose.windowstyler

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.connect
import java.awt.Window
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JWindow

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

// For some reason, passing 0 (fully transparent black) to the setAccentPolicy with
// transparent accent policy results in solid red color. As a workaround, we pass
// fully transparent white which has the same visual effect.
internal fun Color.toAbgrForTransparent() = if (alpha == 0F) 0x00FFFFFF else toAbgr()

// Try hard to get the contentPane
internal var Window.contentPane
    get() = when (this) {
        is JFrame -> contentPane
        is JDialog -> contentPane
        is JWindow -> contentPane
        else -> null
    }
    set(value) = when (this) {
        is JFrame -> contentPane = value
        is JDialog -> contentPane = value
        is JWindow -> contentPane = value
        else -> throw IllegalStateException()
    }