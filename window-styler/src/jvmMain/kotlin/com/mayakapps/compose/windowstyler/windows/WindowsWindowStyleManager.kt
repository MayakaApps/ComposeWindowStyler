/*
 * Copyright 2022-2025 MayakaApps
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mayakapps.compose.windowstyler.windows

import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.isUnspecified
import com.mayakapps.compose.windowstyler.ColorableWindowBackdrop
import com.mayakapps.compose.windowstyler.WindowBackdrop
import com.mayakapps.compose.windowstyler.WindowCornerPreference
import com.mayakapps.compose.windowstyler.WindowFrameStyle
import com.mayakapps.compose.windowstyler.WindowStyleManager
import com.mayakapps.compose.windowstyler.hackContentPane
import com.mayakapps.compose.windowstyler.isTransparent
import com.mayakapps.compose.windowstyler.isUndecorated
import com.mayakapps.compose.windowstyler.setComposeLayerTransparency
import com.mayakapps.compose.windowstyler.windows.jna.Dwm
import com.mayakapps.compose.windowstyler.windows.jna.enums.AccentFlag
import com.mayakapps.compose.windowstyler.windows.jna.enums.AccentState
import com.mayakapps.compose.windowstyler.windows.jna.enums.DwmSystemBackdrop
import com.mayakapps.compose.windowstyler.windows.jna.enums.DwmWindowAttribute
import com.sun.jna.platform.win32.WinDef.HWND
import java.awt.Window
import javax.swing.SwingUtilities

/**
 * Windows implementation of [WindowStyleManager]. It is not recommended to use this class directly.
 *
 * If used on an OS other than Windows, it'll crash.
 */
class WindowsWindowStyleManager(
    window: Window,
    isDarkTheme: Boolean = false,
    backdropType: WindowBackdrop = WindowBackdrop.Default,
    frameStyle: WindowFrameStyle = WindowFrameStyle(),
) : WindowStyleManager {

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
                field = finalValue
                updateBackdrop()
            }
        }

    override var frameStyle: WindowFrameStyle = frameStyle
        set(value) {
            if (field != value) {
                val oldValue = field
                field = value
                updateFrameStyle(oldValue)
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

            updateTheme()
            updateBackdrop()
            updateFrameStyle()
        }
    }

    private fun updateTheme() {
        val attribute = when {
            windowsBuild < 17763 -> return
            windowsBuild >= 18985 -> DwmWindowAttribute.DWMWA_USE_IMMERSIVE_DARK_MODE
            else -> DwmWindowAttribute.DWMWA_USE_IMMERSIVE_DARK_MODE_BEFORE_20H1
        }

        if (Dwm.setWindowAttribute(hwnd, attribute, isDarkTheme)) {
            // Default: This is done to update the background color between white or black
            // ThemedAcrylic: Update the acrylic effect if it is themed
            // Transparent:
            // For some reason, using setImmersiveDarkModeEnabled after setting accent policy to transparent
            // results in solid red backdrop. So, we have to reset the transparent backdrop after using it.
            // This is also required for updating emulated transparent effect
            if (backdropType is WindowBackdrop.Default || backdropType is WindowBackdrop.Acrylic ||
                backdropType is WindowBackdrop.Transparent
            ) updateBackdrop()
            // This is necessary for window buttons to change color correctly
            else if (backdropType is WindowBackdrop.Mica && !isUndecorated) {
                backdropApis.isSheetOfGlassEffectEnabled = true
            }
        }
    }

    private fun updateBackdrop() {
        // This is done to make sure that the window has become visible
        // If the window isn't shown yet, and we try to apply Default, Solid, Aero,
        // or Acrylic, the effect will be applied to the title bar background
        // leaving the caption with awkward background box.
        // Unfortunately, even with this method, mica has this background box.
        SwingUtilities.invokeLater {
            if (windowsBuild >= 22523) {
                backdropApis.systemBackdrop = when (backdropType) {
                    is WindowBackdrop.Mica -> DwmSystemBackdrop.DWMSBT_MAINWINDOW
                    is WindowBackdrop.Acrylic -> DwmSystemBackdrop.DWMSBT_TRANSIENTWINDOW
                    is WindowBackdrop.Tabbed -> DwmSystemBackdrop.DWMSBT_TABBEDWINDOW
                    else -> DwmSystemBackdrop.DWMSBT_AUTO
                }
            } else if (windowsBuild >= 22000) {
                backdropApis.isMicaEffectEnabled = backdropType is WindowBackdrop.Mica
            }

            if (windowsBuild >= 22000 && frameStyle.titleBarColor.isUnspecified) {
                Dwm.setWindowAttribute(
                    hwnd,
                    DwmWindowAttribute.DWMWA_CAPTION_COLOR,
                    when (backdropType) {
                        WindowBackdrop.Default,
                        is WindowBackdrop.Solid,
                        is WindowBackdrop.Transparent,
                        WindowBackdrop.Aero -> Color.Black.toBgr()

                        is WindowBackdrop.Acrylic,
                        is WindowBackdrop.Mica,
                        WindowBackdrop.Tabbed -> 0xFFFFFFFE.toInt() // None
                    },
                )
            }

            val color = when (val backdropType = backdropType) {
                // As the transparency hack is irreversible, the default effect is applied by solid backdrop.
                // The default color is white or black depending on the theme
                is WindowBackdrop.Default -> (if (isDarkTheme) Color.Black else Color.White).toAbgr()
                is WindowBackdrop.Transparent -> backdropType.color.toAbgrForTransparent()
                is ColorableWindowBackdrop -> backdropType.color.toAbgr()
                else -> 0x7FFFFFFF
            }

            backdropApis.setAccentPolicy(
                accentState = when (backdropType) {
                    is WindowBackdrop.Default, is WindowBackdrop.Solid -> AccentState.ACCENT_ENABLE_GRADIENT
                    is WindowBackdrop.Transparent -> AccentState.ACCENT_ENABLE_TRANSPARENTGRADIENT
                    is WindowBackdrop.Aero -> AccentState.ACCENT_ENABLE_BLURBEHIND
                    is WindowBackdrop.Acrylic -> when {
                        windowsBuild >= 22523 -> AccentState.ACCENT_DISABLED
                        else -> AccentState.ACCENT_ENABLE_ACRYLICBLURBEHIND
                    }

                    else -> AccentState.ACCENT_DISABLED
                },
                accentFlags = if (backdropType is WindowBackdrop.Transparent) {
                    setOf(AccentFlag.FLAG_FOR_TRANSPARENCY, AccentFlag.DRAW_ALL_BORDERS)
                } else {
                    setOf(AccentFlag.DRAW_ALL_BORDERS)
                },
                color = color,
            )

            if (windowsBuild >= 17063) {
                backdropApis.isSheetOfGlassEffectEnabled =
                    backdropType is WindowBackdrop.Mica ||
                            backdropType is WindowBackdrop.Tabbed ||
                            backdropType is WindowBackdrop.Acrylic
            }
        }
    }

    /*
     * Frame Style
     */

    private fun updateFrameStyle(oldStyle: WindowFrameStyle? = null) {
        if (windowsBuild >= 22000) {
            if ((oldStyle?.cornerPreference
                    ?: WindowCornerPreference.DEFAULT) != frameStyle.cornerPreference
            ) {
                Dwm.setWindowCornerPreference(
                    hwnd,
                    frameStyle.cornerPreference.toDwmWindowCornerPreference()
                )
            }

            if (frameStyle.borderColor.isSpecified && oldStyle?.borderColor != frameStyle.borderColor) {
                Dwm.setWindowAttribute(
                    hwnd,
                    DwmWindowAttribute.DWMWA_BORDER_COLOR,
                    frameStyle.borderColor.toBgr()
                )
            }

            if (frameStyle.titleBarColor.isSpecified && oldStyle?.titleBarColor != frameStyle.titleBarColor) {
                Dwm.setWindowAttribute(
                    hwnd,
                    DwmWindowAttribute.DWMWA_CAPTION_COLOR,
                    frameStyle.titleBarColor.toBgr()
                )
            }

            if (frameStyle.captionColor.isSpecified && oldStyle?.captionColor != frameStyle.captionColor) {
                Dwm.setWindowAttribute(
                    hwnd,
                    DwmWindowAttribute.DWMWA_TEXT_COLOR,
                    frameStyle.captionColor.toBgr()
                )
            }
        }
    }

    /*
     * Fallback Strategy
     */

    private fun WindowBackdrop.fallbackIfUnsupported(): WindowBackdrop {
        if (windowsBuild >= supportedSince) return this

        return when (this) {
            is WindowBackdrop.Tabbed -> WindowBackdrop.Mica
            is WindowBackdrop.Mica -> themedAcrylic
            is WindowBackdrop.Acrylic -> WindowBackdrop.Transparent(color)

            else -> WindowBackdrop.Default
        }.fallbackIfUnsupported()
    }

    private val themedAcrylic
        get() = WindowBackdrop.Acrylic(themedFallbackColor)

    private val themedFallbackColor
        get() = if (isDarkTheme) Color(0xEF000000L) else Color(0xEFFFFFFFL)

    private val WindowBackdrop.supportedSince
        get() = when (this) {
            is WindowBackdrop.Acrylic -> 17063
            is WindowBackdrop.Mica -> 22000
            is WindowBackdrop.Tabbed -> 22523
            else -> 0
        }
}
