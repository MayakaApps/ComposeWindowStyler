package com.mayakapps.compose.windowstyler

import androidx.compose.runtime.*
import androidx.compose.ui.window.FrameWindowScope
import com.mayakapps.compose.windowstyler.WindowManager.createSheetOfGlassEffect
import com.mayakapps.compose.windowstyler.WindowManager.restoreWindowFrame
import com.mayakapps.compose.windowstyler.WindowManager.setAccentPolicy
import com.mayakapps.compose.windowstyler.WindowManager.setImmersiveDarkModeEnabled
import com.mayakapps.compose.windowstyler.WindowManager.setMicaEffectEnabled
import com.mayakapps.compose.windowstyler.WindowManager.setSystemBackdrop
import com.mayakapps.compose.windowstyler.jna.enums.AccentFlag
import com.mayakapps.compose.windowstyler.jna.enums.AccentState
import javax.swing.UIManager

@Composable
fun FrameWindowScope.WindowStyle(
    isDarkTheme: Boolean = false,
    backdropType: WindowBackdrop = WindowBackdrop.Default,
) {
    LaunchedEffect(Unit) {
        window.setComposeLayerTransparency(true)
        window.hackContentPane()
    }

    val hwnd = remember { window.windowHWND }

    var lastEffect by remember { mutableStateOf<WindowBackdrop?>(null) }

    if (windowsBuild >= 17763) {
        LaunchedEffect(isDarkTheme) {
            setImmersiveDarkModeEnabled(hwnd, isDarkTheme)
        }
    }

    LaunchedEffect(backdropType) {
        // Set [ACCENT_DISABLED] as [ACCENT_POLICY] to apply styles properly.
        setAccentPolicy(hwnd, AccentState.ACCENT_DISABLED)

        if (backdropType is WindowBackdrop.Transparent) return@LaunchedEffect

        // Only on later Windows 11 versions and if effect is WindowEffect.mica,
        // WindowEffect.acrylic or WindowEffect.tabbed, otherwise fallback to old
        // approach.
        if (
            windowsBuild >= 22523 &&
            (backdropType is WindowBackdrop.Acrylic || backdropType is WindowBackdrop.Mica || backdropType is WindowBackdrop.Tabbed)
        ) {
            createSheetOfGlassEffect(hwnd)
            setSystemBackdrop(hwnd, backdropType.toDwmSystemBackdrop())
        } else {
            if (backdropType is WindowBackdrop.Mica) {
                // Check for Windows 11.
                if (windowsBuild >= 22000) {
                    createSheetOfGlassEffect(hwnd)
                    setMicaEffectEnabled(hwnd, true)
                }
            } else {
                // Restore original window style & [DwmExtendFrameIntoClientArea] margin
                // if the last set effect was [WindowEffect.mica], since it sets
                // negative margins to the window.
                if (
                    (windowsBuild >= 22000 && lastEffect is WindowBackdrop.Mica) ||
                    (windowsBuild >= 22523 && (lastEffect is WindowBackdrop.Acrylic || lastEffect is WindowBackdrop.Tabbed))
                ) {
                    restoreWindowFrame(hwnd)
                    setMicaEffectEnabled(hwnd, false)
                }

                setAccentPolicy(
                    hwnd = hwnd,
                    accentState = when (backdropType) {
                        is WindowBackdrop.Default, is WindowBackdrop.Solid -> AccentState.ACCENT_ENABLE_GRADIENT
                        is WindowBackdrop.Aero -> AccentState.ACCENT_ENABLE_BLURBEHIND
                        is WindowBackdrop.Acrylic -> AccentState.ACCENT_ENABLE_ACRYLICBLURBEHIND
                        else -> throw IllegalStateException()
                    },
                    accentFlags = setOf(AccentFlag.DRAW_ALL_BORDERS),
                    color = when (backdropType) {
                        is WindowBackdrop.Default -> UIManager.getColor("Panel.background").rgb
                        is WindowBackdrop.Solid -> backdropType.color
                        is WindowBackdrop.Acrylic -> backdropType.color
                        else -> 0x7FFFFFFF
                    },
                )
            }
        }

        lastEffect = backdropType
    }
}
