package com.mayakapps.compose.windowstyler

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.window.WindowScope

/**
 * Applies the provided styles to the current window.
 *
 * See [WindowStyleManager.isDarkTheme], [WindowBackdrop], [WindowFrameStyle].
 */
@Composable
fun WindowScope.WindowStyle(
    isDarkTheme: Boolean = false,
    backdropType: WindowBackdrop = WindowBackdrop.Default,
    frameStyle: WindowFrameStyle = WindowFrameStyle(),
    manageTitlebar: Boolean = false,
) {
    val manager = remember { WindowStyleManager(window, isDarkTheme, backdropType, frameStyle, manageTitlebar) }

    LaunchedEffect(isDarkTheme) {
        manager.isDarkTheme = isDarkTheme
    }

    LaunchedEffect(backdropType) {
        manager.backdropType = backdropType
    }
}