package com.mayakapps.compose.windowstyler

import androidx.compose.runtime.*
import androidx.compose.ui.window.FrameWindowScope
import com.mayakapps.compose.windowstyler.jna.*
import com.mayakapps.compose.windowstyler.jna.enums.AccentFlag
import com.mayakapps.compose.windowstyler.jna.enums.AccentState
import com.mayakapps.compose.windowstyler.jna.enums.DwmWindowAttribute
import com.mayakapps.compose.windowstyler.jna.enums.WindowCompositionAttribute
import com.mayakapps.compose.windowstyler.jna.structs.AccentPolicy
import com.mayakapps.compose.windowstyler.jna.structs.Margins
import com.mayakapps.compose.windowstyler.jna.structs.OsVersionInfo
import com.mayakapps.compose.windowstyler.jna.structs.WindowCompositionAttributeData
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.ptr.IntByReference
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

    val windowsBuild = remember {
        val osVersionInfo = OsVersionInfo()
        Nt.RtlGetVersion(osVersionInfo)
        osVersionInfo.buildNumber
    }

    var lastEffect by remember { mutableStateOf<WindowEffect?>(null) }

    LaunchedEffect(Unit) {
        setComposeLayerTransparency(true)
        hackContentPane()
    }

    LaunchedEffect(effect) {
        // Set [ACCENT_DISABLED] as [ACCENT_POLICY] in
        // [SetWindowCompositionAttribute] to apply styles properly.
        val data = WindowCompositionAttributeData(
            WindowCompositionAttribute.WCA_ACCENT_POLICY,
            AccentPolicy(AccentState.ACCENT_DISABLED),
        )

        User32.SetWindowCompositionAttribute(hwnd, data)

        if (effect is WindowEffect.Transparent) return@LaunchedEffect

        // Only on later Windows 11 versions and if effect is WindowEffect.mica,
        // WindowEffect.acrylic or WindowEffect.tabbed, otherwise fallback to old
        // approach.
        if (
            windowsBuild >= 22523 &&
            (effect is WindowEffect.Acrylic || effect is WindowEffect.Mica || effect is WindowEffect.Tabbed)
        ) {
            val margins = Margins(leftWidth = -1, rightWidth = -1, topHeight = -1, bottomHeight = -1)
            val isDark = WinDef.BOOLByReference(WinDef.BOOL((effect as? WindowEffect.Mica)?.isDark ?: false))
            val effectValue = IntByReference(
                when (effect) {
                    is WindowEffect.Mica -> 2
                    is WindowEffect.Acrylic -> 3
                    is WindowEffect.Tabbed -> 4
                    else -> throw IllegalStateException()
                }
            )

            Dwm.DwmExtendFrameIntoClientArea(hwnd, margins)
            Dwm.DwmSetWindowAttribute(hwnd, DwmWindowAttribute.DWMWA_USE_IMMERSIVE_DARK_MODE, isDark, 4)
            Dwm.DwmSetWindowAttribute(hwnd, DwmWindowAttribute.DWMWA_SYSTEMBACKDROP_TYPE, effectValue, 4)
        } else {
            if (effect is WindowEffect.Mica) {
                // Check for Windows 11.
                if (windowsBuild >= 22000) {
                    val margins = Margins(leftWidth = -1, rightWidth = -1, topHeight = -1, bottomHeight = -1)
                    val isDark = WinDef.BOOLByReference(WinDef.BOOL(effect.isDark))
                    val enabled = WinDef.BOOLByReference(WinDef.BOOL(true))

                    Dwm.DwmExtendFrameIntoClientArea(hwnd, margins)
                    Dwm.DwmSetWindowAttribute(hwnd, DwmWindowAttribute.DWMWA_USE_IMMERSIVE_DARK_MODE, isDark, 4)
                    Dwm.DwmSetWindowAttribute(hwnd, DwmWindowAttribute.DWMWA_MICA_EFFECT, enabled, 4)
                }
            } else {
                // Restore original window style & [DwmExtendFrameIntoClientArea] margin
                // if the last set effect was [WindowEffect.mica], since it sets
                // negative margins to the window.
                if (
                    (windowsBuild >= 22000 && lastEffect is WindowEffect.Mica) ||
                    (windowsBuild >= 22523 && (lastEffect is WindowEffect.Acrylic || lastEffect is WindowEffect.Tabbed))
                ) {
                    // Atleast one margin should be non-negative in order to show the DWM
                    // window shadow created by handling [WM_NCCALCSIZE].
                    //
                    // Matching value with bitsdojo_window.
                    // https://github.com/bitsdojo/bitsdojo_window/blob/adad0cd40be3d3e12df11d864f18a96a2d0fb4fb/bitsdojo_window_windows/windows/bitsdojo_window.cpp#L149
                    val margins = Margins(leftWidth = 0, rightWidth = 0, topHeight = 1, bottomHeight = 0)
                    val enabled = WinDef.BOOLByReference(WinDef.BOOL(false))

                    Dwm.DwmExtendFrameIntoClientArea(hwnd, margins)
                    Dwm.DwmSetWindowAttribute(hwnd, DwmWindowAttribute.DWMWA_USE_IMMERSIVE_DARK_MODE, enabled, 4)
                    Dwm.DwmSetWindowAttribute(hwnd, DwmWindowAttribute.DWMWA_MICA_EFFECT, enabled, 4)
                }

                val accent = AccentPolicy(
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

                val accentData = WindowCompositionAttributeData(
                    WindowCompositionAttribute.WCA_ACCENT_POLICY,
                    accent,
                )

                User32.SetWindowCompositionAttribute(hwnd, accentData)
            }
        }

        lastEffect = effect
    }
}