package com.mayakapps.compose.windowstyler

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.window.WindowScope

@Composable
fun WindowScope.WindowStyle(
    isDarkTheme: Boolean = false,
    backdropType: WindowBackdrop = WindowBackdrop.Default,
    frameStyle: WindowFrameStyle = WindowFrameStyle(),
) {
    val manager = remember { WindowManager(window, isDarkTheme, backdropType, frameStyle) }

    LaunchedEffect(isDarkTheme) {
        manager.isDarkTheme = isDarkTheme
    }

    LaunchedEffect(backdropType) {
        manager.backdropType = backdropType
    }
}