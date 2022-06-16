package com.mayakapps.compose.windowstyler

import com.mayakapps.compose.windowstyler.windows.WindowsWindowManager
import java.awt.Window

fun WindowManager(
    window: Window,
    isDarkTheme: Boolean = false,
    backdropType: WindowBackdrop = WindowBackdrop.Default,
) = when (OsType.current) {
    OsType.WINDOWS -> WindowsWindowManager(window, isDarkTheme, backdropType)
    else -> StubWindowManager(isDarkTheme, backdropType)
}

interface WindowManager {
    var isDarkTheme: Boolean
    var backdropType: WindowBackdrop
}

internal class StubWindowManager(
    override var isDarkTheme: Boolean,
    override var backdropType: WindowBackdrop,
) : WindowManager