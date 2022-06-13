package com.mayakapps.compose.windowstyler

import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import com.mayakapps.compose.windowstyler.jna.enums.AccentFlag
import com.mayakapps.compose.windowstyler.jna.enums.AccentState
import com.mayakapps.compose.windowstyler.jna.enums.DwmSystemBackdrop
import com.sun.jna.platform.win32.WinDef.HWND
import javax.swing.JFrame
import javax.swing.SwingUtilities

class WindowManager(
    window: JFrame,
    isDarkTheme: Boolean = false,
    backdropType: WindowBackdrop = WindowBackdrop.Default,
) {

    private val hwnd: HWND = window.hwnd

    var isDarkTheme: Boolean = isDarkTheme
        set(value) {
            if (field != value) {
                field = value
                updateTheme()
            }
        }

    var backdropType: WindowBackdrop = backdropType
        set(value) {
            val finalValue = value.fallbackIfUnsupported()

            if (field != finalValue) {
                field = finalValue
                updateBackdrop()
            }
        }

    init {
        // invokeLater is called to make sure that ComposeLayer was initialized first
        SwingUtilities.invokeLater {
            // For some reason, reversing the order of these two calls doesn't work.
            if (window is ComposeWindow) window.setComposeLayerTransparency(true)
            window.hackContentPane()

            updateTheme()
            updateBackdrop()
        }
    }

    private var isSystemBackdropSet = false
    private var isMicaEnabled = false
    private var isAccentPolicySet = false
    private var isSheetOfGlassApplied = false

    private fun updateTheme() {
        if (windowsBuild >= 17763 && Native.setImmersiveDarkModeEnabled(hwnd, isDarkTheme)) {
            when (backdropType) {
                // For some reason, using setImmersiveDarkModeEnabled after setting accent policy to transparent
                // results in solid red backdrop. So, we have to reset the transparent backdrop after using it.
                // This is also required for updating emulated transparent effect
                is WindowBackdrop.Transparent -> {
                    resetAccentPolicy()
                    if (backdropType === lightTransparent || backdropType === darkTransparent) {
                        backdropType = themedTransparent
                    } else updateBackdrop()
                }

                // This is done to update the background color between white or black
                is WindowBackdrop.Default -> updateBackdrop()

                // Update the acrylic effect if it is an emulated effect
                is WindowBackdrop.Acrylic -> {
                    if (backdropType === lightAcrylic || backdropType === darkAcrylic) backdropType = themedAcrylic
                }

                else -> {
                    /* No action needed */
                }
            }
        }
    }

    private fun updateBackdrop() {
        // This is done to make sure that the window has become visible
        // If the window isn't shown yet, and we try to apply Default, Solid, Aero,
        // or Acrylic, the effect will be applied to the title bar background
        // leaving the caption with awkward background box.
        // Unfortunately, even with this method, mica in light mode has this
        // background box.
        SwingUtilities.invokeLater {
            // Only on later Windows 11 versions and if effect is WindowEffect.mica,
            // WindowEffect.acrylic or WindowEffect.tabbed, otherwise fallback to old
            // approach.
            if (
                windowsBuild >= 22523 &&
                (backdropType is WindowBackdrop.Acrylic || backdropType is WindowBackdrop.Mica || backdropType is WindowBackdrop.Tabbed)
            ) {
                setSystemBackdrop(backdropType.toDwmSystemBackdrop())
            } else {
                if (backdropType is WindowBackdrop.Mica) {
                    // Check for Windows 11.
                    if (windowsBuild >= 22000) {
                        setMicaEffectEnabled(true)
                    }
                } else {
                    val color = when (val backdropType = backdropType) {
                        // As the transparency hack is irreversible, the default effect is applied by solid backdrop.
                        // The default color is white or black depending on the theme
                        is WindowBackdrop.Default -> (if (isDarkTheme) Color.Black else Color.White).toAbgr()
                        is WindowBackdrop.Transparent -> backdropType.color.toAbgrForTransparent()
                        is ColorableWindowBackdrop -> backdropType.color.toAbgr()
                        else -> 0x7FFFFFFF
                    }

                    setAccentPolicy(
                        accentState = backdropType.toAccentState(),
                        accentFlags = setOf(AccentFlag.DRAW_ALL_BORDERS),
                        color = color,
                    )
                }
            }
        }
    }


    private fun setSystemBackdrop(systemBackdrop: DwmSystemBackdrop) {
        createSheetOfGlassEffect()
        if (Native.setSystemBackdrop(hwnd, systemBackdrop)) {
            isSystemBackdropSet = systemBackdrop == DwmSystemBackdrop.DWMSBT_DISABLE
            if (isSystemBackdropSet) resetAccentPolicy()
        }
    }

    private fun setMicaEffectEnabled(enabled: Boolean) {
        createSheetOfGlassEffect()
        if (Native.setMicaEffectEnabled(hwnd, enabled)) {
            isMicaEnabled = enabled
            if (isMicaEnabled) resetAccentPolicy()
        }
    }

    private fun setAccentPolicy(
        accentState: AccentState = AccentState.ACCENT_DISABLED,
        accentFlags: Set<AccentFlag> = emptySet(),
        color: Int = 0,
        animationId: Int = 0,
    ) {
        if (Native.setAccentPolicy(hwnd, accentState, accentFlags, color, animationId)) {
            isAccentPolicySet = accentState != AccentState.ACCENT_DISABLED
            if (isAccentPolicySet) {
                resetSystemBackdrop()
                resetMicaEffectEnabled()
                resetWindowFrame()
            }
        }
    }

    private fun createSheetOfGlassEffect() {
        if (!isSheetOfGlassApplied && Native.createSheetOfGlassEffect(hwnd)) isSheetOfGlassApplied = true
    }


    private fun resetSystemBackdrop() {
        if (isSystemBackdropSet) setSystemBackdrop(DwmSystemBackdrop.DWMSBT_DISABLE)
    }

    private fun resetMicaEffectEnabled() {
        if (isMicaEnabled) setMicaEffectEnabled(false)
    }

    private fun resetAccentPolicy() {
        if (isAccentPolicySet) setAccentPolicy(AccentState.ACCENT_DISABLED)
    }

    private fun resetWindowFrame() {
        if (isSheetOfGlassApplied && Native.restoreWindowFrame(hwnd)) isSheetOfGlassApplied = false
    }


    private fun WindowBackdrop.fallbackIfUnsupported(): WindowBackdrop {
        if (windowsBuild >= supportedSince) return this

        return when (this) {
            is WindowBackdrop.Tabbed -> WindowBackdrop.Mica
            is WindowBackdrop.Mica -> themedAcrylic
            is WindowBackdrop.Acrylic -> {
                // Aero isn't customizable and too transparent for background
                // Manual mapping of themedAcrylic is to keep the theming working as expected
                if (this === lightAcrylic || this === darkAcrylic) themedTransparent
                else WindowBackdrop.Transparent(color)
            }
            else -> WindowBackdrop.Default
        }.fallbackIfUnsupported()
    }

    private val lightFallback = Color(0xEFFFFFFFL)
    private val darkFallback = Color(0xEF000000L)

    private val lightAcrylic = WindowBackdrop.Acrylic(lightFallback)
    private val darkAcrylic = WindowBackdrop.Acrylic(darkFallback)
    private val themedAcrylic get() = if (isDarkTheme) darkAcrylic else lightAcrylic

    private val lightTransparent = WindowBackdrop.Transparent(lightFallback)
    private val darkTransparent = WindowBackdrop.Transparent(darkFallback)
    private val themedTransparent get() = if (isDarkTheme) darkTransparent else lightTransparent
}