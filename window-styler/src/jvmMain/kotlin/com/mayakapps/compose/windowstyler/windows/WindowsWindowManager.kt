package com.mayakapps.compose.windowstyler.windows

import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import com.mayakapps.compose.windowstyler.*
import com.mayakapps.compose.windowstyler.windows.jna.Dwm
import com.mayakapps.compose.windowstyler.windows.jna.enums.AccentFlag
import com.mayakapps.compose.windowstyler.windows.jna.enums.DwmWindowAttribute
import com.sun.jna.platform.win32.WinDef.HWND
import java.awt.Window
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.SwingUtilities

class WindowsWindowManager(
    window: Window,
    isDarkTheme: Boolean = false,
    backdropType: WindowBackdrop = WindowBackdrop.Default,
) : WindowManager {

    private val hwnd: HWND = window.hwnd
    private val isUndecorated = window.isUndecorated

    private val backdropApis = WindowsBackdropApis(hwnd)

    override var isDarkTheme: Boolean = isDarkTheme
        set(value) {
            if (field != value) {
                field = value
                updateTheme()
            }
        }

    override var backdropType: WindowBackdrop = backdropType
        set(value) {
            val finalValue = value.fallbackIfUnsupported()

            if (field != finalValue) {
                wasAero = field is WindowBackdrop.Aero
                field = finalValue
                updateBackdrop()
            }
        }

    // This is a workaround for transparency getting replaced by red opaque color for decorated windows on focus
    // changes. This workaround doesn't appear to be efficient, and there may be red flashes on losing/gaining focus.
    // Yet, it seems to be enough for the limited use cases of transparent decorated
    private val windowAdapter = object : WindowAdapter() {
        override fun windowGainedFocus(e: WindowEvent?) = resetTransparent()
        override fun windowLostFocus(e: WindowEvent?) = resetTransparent()

        private fun resetTransparent() {
            if (!isUndecorated && this@WindowsWindowManager.backdropType is WindowBackdrop.Transparent) updateBackdrop()
        }
    }

    init {
        // invokeLater is called to make sure that ComposeLayer was initialized first
        SwingUtilities.invokeLater {
            // If the window is not already transparent, hack it to be transparent
            if (!window.isTransparent) {
                // For some reason, reversing the order of these two calls doesn't work.
                if (window is ComposeWindow) window.setComposeLayerTransparency(true)
                window.hackContentPane()
            }

            window.addWindowFocusListener(windowAdapter)

            updateTheme()
            updateBackdrop()
        }
    }

    private var wasAero = false

    private fun updateTheme() {
        val attribute =
            when {
                windowsBuild < 17763 -> return
                windowsBuild >= 18985 -> DwmWindowAttribute.DWMWA_USE_IMMERSIVE_DARK_MODE
                else -> DwmWindowAttribute.DWMWA_USE_IMMERSIVE_DARK_MODE_BEFORE_20H1
            }

        if (windowsBuild >= 17763 && Dwm.setWindowAttribute(hwnd, attribute, isDarkTheme)) {
            // Default: This is done to update the background color between white or black
            // ThemedAcrylic: Update the acrylic effect if it is themed
            // Transparent:
            // For some reason, using setImmersiveDarkModeEnabled after setting accent policy to transparent
            // results in solid red backdrop. So, we have to reset the transparent backdrop after using it.
            // This is also required for updating emulated transparent effect
            if (backdropType is WindowBackdrop.Default || backdropType is ThemedAcrylic ||
                backdropType is WindowBackdrop.Transparent
            ) updateBackdrop()
        }
    }

    private fun updateBackdrop() {
        // This is done to make sure that the window has become visible
        // If the window isn't shown yet, and we try to apply Default, Solid, Aero,
        // or Acrylic, the effect will be applied to the title bar background
        // leaving the caption with awkward background box.
        // Unfortunately, even with this method, mica has this background box.
        SwingUtilities.invokeLater {
            // Only on later Windows 11 versions and if effect is WindowEffect.mica,
            // WindowEffect.acrylic or WindowEffect.tabbed, otherwise fallback to old
            // approach.
            if (
                windowsBuild >= 22523 &&
                (backdropType is WindowBackdrop.Acrylic || backdropType is WindowBackdrop.Mica || backdropType is WindowBackdrop.Tabbed)
            ) {
                backdropApis.setSystemBackdrop(backdropType.toDwmSystemBackdrop())
            } else {
                if (backdropType is WindowBackdrop.Mica) {
                    // Check for Windows 11.
                    if (windowsBuild >= 22000) {
                        backdropApis.setMicaEffectEnabled(true)
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

                    // wasAero: This is required as sometimes the window gets stuck at aero
                    // Transparent: In many cases, if this is not done, red opaque background is shown
                    if (wasAero || backdropType is WindowBackdrop.Transparent) backdropApis.resetAccentPolicy()

                    // Another red opaque background case :'(
                    // Resetting these values needs to be done before applying transparency
                    if (backdropType is WindowBackdrop.Transparent) {
                        backdropApis.resetMicaEffectEnabled()
                        backdropApis.resetSystemBackdrop()
                    }

                    backdropApis.setAccentPolicy(
                        accentState = backdropType.toAccentState(),
                        accentFlags = setOf(AccentFlag.DRAW_ALL_BORDERS),
                        color = color,
                    )
                }
            }
        }
    }

    private fun WindowBackdrop.fallbackIfUnsupported(): WindowBackdrop {
        if (windowsBuild >= supportedSince) return this

        return when (this) {
            is WindowBackdrop.Tabbed -> WindowBackdrop.Mica
            is WindowBackdrop.Mica -> themedAcrylic
            is WindowBackdrop.Acrylic -> {
                // Aero isn't customizable and too transparent for background
                // Manual mapping of themedAcrylic is to keep the theming working as expected
                if (this is ThemedAcrylic) themedTransparent
                else WindowBackdrop.Transparent(color)
            }
            else -> WindowBackdrop.Default
        }.fallbackIfUnsupported()
    }

    private val themedTransparent = ThemedTransparent()
    private val themedAcrylic = ThemedAcrylic()

    private val themedFallbackColor
        get() = if (isDarkTheme) Color(0xEF000000L) else Color(0xEFFFFFFFL)

    inner class ThemedAcrylic : WindowBackdrop.Acrylic(Color.Unspecified) {
        override val color: Color
            get() = themedFallbackColor
    }

    inner class ThemedTransparent : WindowBackdrop.Transparent(Color.Unspecified) {
        override val color: Color
            get() = themedFallbackColor
    }
}