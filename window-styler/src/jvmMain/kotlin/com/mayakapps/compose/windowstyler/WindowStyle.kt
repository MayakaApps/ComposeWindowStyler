package com.mayakapps.compose.windowstyler

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.window.FrameWindowScope

@Composable
fun FrameWindowScope.WindowStyle(
    isDarkTheme: Boolean = false,
    backdropType: WindowBackdrop = WindowBackdrop.Default,
) {
    val manager = remember { WindowManager(window, isDarkTheme, backdropType) }

    LaunchedEffect(isDarkTheme) {
        manager.isDarkTheme = isDarkTheme
    }

    LaunchedEffect(backdropType) {
        manager.backdropType = backdropType
    }
}