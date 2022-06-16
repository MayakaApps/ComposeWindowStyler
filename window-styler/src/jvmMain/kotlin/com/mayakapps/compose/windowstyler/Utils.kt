package com.mayakapps.compose.windowstyler

import java.awt.Window
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JWindow

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