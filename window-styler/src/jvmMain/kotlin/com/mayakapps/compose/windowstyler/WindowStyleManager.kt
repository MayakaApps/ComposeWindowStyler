package com.mayakapps.compose.windowstyler

import com.mayakapps.compose.windowstyler.windows.WindowsWindowStyleManager
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs
import java.awt.Window

fun WindowStyleManager(
    window: Window,
    isDarkTheme: Boolean = false,
    backdropType: WindowBackdrop = WindowBackdrop.Default,
    frameStyle: WindowFrameStyle = WindowFrameStyle(),
) = when (hostOs) {
    OS.Windows -> WindowsWindowStyleManager(window, isDarkTheme, backdropType, frameStyle)
    else -> StubWindowStyleManager(isDarkTheme, backdropType, frameStyle)
}

interface WindowStyleManager {
    var isDarkTheme: Boolean
    var backdropType: WindowBackdrop
    var frameStyle: WindowFrameStyle
}

internal class StubWindowStyleManager(
    override var isDarkTheme: Boolean,
    override var backdropType: WindowBackdrop,
    override var frameStyle: WindowFrameStyle,
) : WindowStyleManager