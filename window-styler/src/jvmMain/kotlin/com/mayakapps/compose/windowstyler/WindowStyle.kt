package com.mayakapps.compose.windowstyler

import androidx.compose.runtime.*
import androidx.compose.ui.window.FrameWindowScope
import com.mayakapps.compose.windowstyler.jna.enums.AccentFlag
import com.mayakapps.compose.windowstyler.jna.enums.AccentState
import javax.swing.UIManager

@Composable
fun FrameWindowScope.WindowStyle(backdrop: WindowBackdrop) = window.apply {
    val hwnd = remember { windowHWND }

    var lastEffect by remember { mutableStateOf<WindowBackdrop?>(null) }

    LaunchedEffect(Unit) {
        setComposeLayerTransparency(true)
        hackContentPane()
    }

    LaunchedEffect(backdrop) {
        // Set [ACCENT_DISABLED] as [ACCENT_POLICY] to apply styles properly.
        WindowManager.setAccentPolicy(hwnd, AccentState.ACCENT_DISABLED)

        if (backdrop is WindowBackdrop.Transparent) return@LaunchedEffect

        // Only on later Windows 11 versions and if effect is WindowEffect.mica,
        // WindowEffect.acrylic or WindowEffect.tabbed, otherwise fallback to old
        // approach.
        if (
            windowsBuild >= 22523 &&
            (backdrop is WindowBackdrop.Acrylic || backdrop is WindowBackdrop.Mica || backdrop is WindowBackdrop.Tabbed)
        ) {
            WindowManager.createSheetOfGlassEffect(hwnd)
            WindowManager.setImmersiveDarkModeEnabled(hwnd, (backdrop as? WindowBackdrop.Mica)?.isDark ?: false)
            WindowManager.setSystemBackdrop(hwnd, backdrop.toDwmSystemBackdrop())
        } else {
            if (backdrop is WindowBackdrop.Mica) {
                // Check for Windows 11.
                if (windowsBuild >= 22000) {
                    WindowManager.createSheetOfGlassEffect(hwnd)
                    WindowManager.setImmersiveDarkModeEnabled(hwnd, backdrop.isDark)
                    WindowManager.setMicaEffectEnabled(hwnd, true)
                }
            } else {
                // Restore original window style & [DwmExtendFrameIntoClientArea] margin
                // if the last set effect was [WindowEffect.mica], since it sets
                // negative margins to the window.
                if (
                    (windowsBuild >= 22000 && lastEffect is WindowBackdrop.Mica) ||
                    (windowsBuild >= 22523 && (lastEffect is WindowBackdrop.Acrylic || lastEffect is WindowBackdrop.Tabbed))
                ) {
                    WindowManager.restoreWindowFrame(hwnd)
                    WindowManager.setImmersiveDarkModeEnabled(hwnd, false)
                    WindowManager.setMicaEffectEnabled(hwnd, false)
                }

                WindowManager.setAccentPolicy(
                    hwnd = hwnd,
                    accentState = when (backdrop) {
                        is WindowBackdrop.Default, is WindowBackdrop.Solid -> AccentState.ACCENT_ENABLE_GRADIENT
                        is WindowBackdrop.Aero -> AccentState.ACCENT_ENABLE_BLURBEHIND
                        is WindowBackdrop.Acrylic -> AccentState.ACCENT_ENABLE_ACRYLICBLURBEHIND
                        else -> throw IllegalStateException()
                    },
                    accentFlags = setOf(AccentFlag.DRAW_ALL_BORDERS),
                    color = when (backdrop) {
                        is WindowBackdrop.Default -> UIManager.getColor("Panel.background").rgb
                        is WindowBackdrop.Solid -> backdrop.color
                        is WindowBackdrop.Acrylic -> backdrop.color
                        else -> 0x7FFFFFFF
                    },
                )
            }
        }

        lastEffect = backdrop
    }
}
