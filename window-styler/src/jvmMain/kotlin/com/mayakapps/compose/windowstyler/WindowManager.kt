package com.mayakapps.compose.windowstyler

import com.mayakapps.compose.windowstyler.windows.WindowsWindowManager
import java.awt.Window

fun WindowManager(
    window: Window,
    isDarkTheme: Boolean = false,
    backdropType: WindowBackdrop = WindowBackdrop.Default,
    frameStyle: WindowFrameStyle = WindowFrameStyle(),
) = when (OsType.current) {
    OsType.WINDOWS -> WindowsWindowManager(window, isDarkTheme, backdropType, frameStyle)
    else -> StubWindowManager(isDarkTheme, backdropType, frameStyle)
}

interface WindowManager {
    var isDarkTheme: Boolean
    var backdropType: WindowBackdrop
    var frameStyle: WindowFrameStyle
}

internal class StubWindowManager(
    override var isDarkTheme: Boolean,
    override var backdropType: WindowBackdrop,
    override var frameStyle: WindowFrameStyle,
) : WindowManager