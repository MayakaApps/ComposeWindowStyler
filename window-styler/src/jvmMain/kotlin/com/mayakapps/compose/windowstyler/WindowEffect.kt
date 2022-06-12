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

sealed interface WindowEffect {
    object Default : WindowEffect
    object Transparent : WindowEffect
    data class Solid(val color: Int) : WindowEffect
    object Aero : WindowEffect
    data class Acrylic(val color: Int) : WindowEffect
    data class Mica(val isDark: Boolean) : WindowEffect
    object Tabbed : WindowEffect
}

@Composable
fun FrameWindowScope.ApplyEffect(effect: WindowEffect) = window.apply {
    val hwnd = remember { windowHWND }

    var lastEffect by remember { mutableStateOf<WindowEffect?>(null) }

    LaunchedEffect(Unit) {
        setComposeLayerTransparency(true)
        hackContentPane()
    }

    LaunchedEffect(effect) {
        // Set [ACCENT_DISABLED] as [ACCENT_POLICY] to apply styles properly.
        setAccentPolicy(hwnd, AccentState.ACCENT_DISABLED)

        if (effect is WindowEffect.Transparent) return@LaunchedEffect

        // Only on later Windows 11 versions and if effect is WindowEffect.mica,
        // WindowEffect.acrylic or WindowEffect.tabbed, otherwise fallback to old
        // approach.
        if (
            windowsBuild >= 22523 &&
            (effect is WindowEffect.Acrylic || effect is WindowEffect.Mica || effect is WindowEffect.Tabbed)
        ) {
            createSheetOfGlassEffect(hwnd)
            setImmersiveDarkModeEnabled(hwnd, (effect as? WindowEffect.Mica)?.isDark ?: false)
            setSystemBackdrop(hwnd, effect.toDwmSystemBackdrop())
        } else {
            if (effect is WindowEffect.Mica) {
                // Check for Windows 11.
                if (windowsBuild >= 22000) {
                    createSheetOfGlassEffect(hwnd)
                    setImmersiveDarkModeEnabled(hwnd, effect.isDark)
                    setMicaEffectEnabled(hwnd, true)
                }
            } else {
                // Restore original window style & [DwmExtendFrameIntoClientArea] margin
                // if the last set effect was [WindowEffect.mica], since it sets
                // negative margins to the window.
                if (
                    (windowsBuild >= 22000 && lastEffect is WindowEffect.Mica) ||
                    (windowsBuild >= 22523 && (lastEffect is WindowEffect.Acrylic || lastEffect is WindowEffect.Tabbed))
                ) {
                    restoreWindowFrame(hwnd)
                    setImmersiveDarkModeEnabled(hwnd, false)
                    setMicaEffectEnabled(hwnd, false)
                }

                setAccentPolicy(
                    hwnd = hwnd,
                    accentState = when (effect) {
                        is WindowEffect.Default, is WindowEffect.Solid -> AccentState.ACCENT_ENABLE_GRADIENT
                        is WindowEffect.Aero -> AccentState.ACCENT_ENABLE_BLURBEHIND
                        is WindowEffect.Acrylic -> AccentState.ACCENT_ENABLE_ACRYLICBLURBEHIND
                        else -> throw IllegalStateException()
                    },
                    accentFlags = setOf(AccentFlag.DRAW_ALL_BORDERS),
                    color = when (effect) {
                        is WindowEffect.Default -> UIManager.getColor("Panel.background").rgb
                        is WindowEffect.Solid -> effect.color
                        is WindowEffect.Acrylic -> effect.color
                        else -> 0x7FFFFFFF
                    },
                )
            }
        }

        lastEffect = effect
    }
}