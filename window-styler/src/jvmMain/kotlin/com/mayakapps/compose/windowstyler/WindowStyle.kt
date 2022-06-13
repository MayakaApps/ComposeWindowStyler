package com.mayakapps.compose.windowstyler

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.FrameWindowScope
import com.mayakapps.compose.windowstyler.WindowManager.createSheetOfGlassEffect
import com.mayakapps.compose.windowstyler.WindowManager.restoreWindowFrame
import com.mayakapps.compose.windowstyler.WindowManager.setAccentPolicy
import com.mayakapps.compose.windowstyler.WindowManager.setImmersiveDarkModeEnabled
import com.mayakapps.compose.windowstyler.WindowManager.setMicaEffectEnabled
import com.mayakapps.compose.windowstyler.WindowManager.setSystemBackdrop
import com.mayakapps.compose.windowstyler.jna.enums.AccentFlag
import com.mayakapps.compose.windowstyler.jna.enums.AccentState
import javax.swing.SwingUtilities

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

    var forceUpdateEffect by remember { mutableStateOf(0) }
    var lastEffect by remember { mutableStateOf<WindowBackdrop?>(null) }

    if (windowsBuild >= 17763) {
        LaunchedEffect(isDarkTheme) {
            setImmersiveDarkModeEnabled(hwnd, isDarkTheme)

            when (lastEffect) {
                // For some reason, using setImmersiveDarkModeEnabled after setting accent policy to transparent
                // results in solid red backdrop. So, we have to reset the transparent backdrop after using it.
                is WindowBackdrop.Transparent -> {
                    setAccentPolicy(hwnd, AccentState.ACCENT_DISABLED)
                    forceUpdateEffect++
                }

                // This is done to update the background color between white or black
                is WindowBackdrop.Default -> forceUpdateEffect++

                else -> {
                    /* No action needed */
                }
            }
        }
    }

    LaunchedEffect(backdropType, forceUpdateEffect) {
        // This is done to make sure that the window has become visible
        // If the window isn't shown yet, and we try to apply Default, Solid, Aero,
        // or Acrylic, the effect will be applied to the title bar background
        // leaving the caption with awkward background box.
        // Unfortunately, even with this method, mica in light mode has this
        // background box.
        SwingUtilities.invokeLater {
            // Set [ACCENT_DISABLED] as [ACCENT_POLICY] to apply styles properly.
            setAccentPolicy(hwnd, AccentState.ACCENT_DISABLED)

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

                    val color = when (backdropType) {
                        // As the transparency hack is irreversible, the default effect is applied by solid backdrop.
                        // The default color is white or black depending on the theme
                        is WindowBackdrop.Default -> (if (isDarkTheme) Color.Black else Color.White).toAbgr()
                        is WindowBackdrop.Transparent -> backdropType.color.toAbgrForTransparent()
                        is ColorableWindowBackdrop -> backdropType.color.toAbgr()
                        else -> 0x7FFFFFFF
                    }

                    setAccentPolicy(
                        hwnd = hwnd,
                        accentState = backdropType.toAccentState(),
                        accentFlags = setOf(AccentFlag.DRAW_ALL_BORDERS),
                        color = color,
                    )
                }
            }

            lastEffect = backdropType
        }
    }
}