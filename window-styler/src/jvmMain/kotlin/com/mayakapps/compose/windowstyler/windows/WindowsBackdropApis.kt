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

import com.mayakapps.compose.windowstyler.windows.jna.Dwm
import com.mayakapps.compose.windowstyler.windows.jna.User32
import com.mayakapps.compose.windowstyler.windows.jna.enums.AccentFlag
import com.mayakapps.compose.windowstyler.windows.jna.enums.AccentState
import com.mayakapps.compose.windowstyler.windows.jna.enums.DwmSystemBackdrop
import com.mayakapps.compose.windowstyler.windows.jna.enums.DwmWindowAttribute
import com.sun.jna.platform.win32.WinDef

internal class WindowsBackdropApis(private val hwnd: WinDef.HWND) {

    var systemBackdrop: DwmSystemBackdrop? = null
        set(value) {
            requireNotNull(value)
            if (field == value) return

            val result = Dwm.setSystemBackdrop(hwnd, value)
            if (result) field = value
        }

    var isMicaEffectEnabled: Boolean? = null
        set(value) {
            requireNotNull(value)
            if (field == value) return

            val result = Dwm.setWindowAttribute(hwnd, DwmWindowAttribute.DWMWA_MICA_EFFECT, value)
            if (result) field = value
        }

    var isSheetOfGlassEffectEnabled: Boolean? = null
        set(value) {
            requireNotNull(value)

            val result = if (value) {
                // Negative margins have special meaning to DwmExtendFrameIntoClientArea.
                // Negative margins create the "sheet of glass" effect, where the client area is
                // rendered as a solid surface with no window border.
                Dwm.extendFrameIntoClientArea(hwnd = hwnd, margin = -1)
            } else {
                // At least one margin should be non-negative in order to show the DWM window shadow
                // created by handling [WM_NCCALCSIZE]. Matching value with bitsdojo_window:
                // https://github.com/bitsdojo/bitsdojo_window/blob/adad0cd40be3d3e12df11d864f18a96a2d0fb4fb/bitsdojo_window_windows/windows/bitsdojo_window.cpp#L149
                Dwm.extendFrameIntoClientArea(
                    hwnd = hwnd,
                    leftWidth = 0,
                    rightWidth = 0,
                    topHeight = 1,
                    bottomHeight = 0,
                )
            }

            if (result) field = value
        }

    fun setAccentPolicy(
        accentState: AccentState = AccentState.ACCENT_DISABLED,
        accentFlags: Set<AccentFlag> = emptySet(),
        color: Int = 0,
        animationId: Int = 0,
    ) {
        User32.setAccentPolicy(hwnd, accentState, accentFlags, color, animationId)
    }
}
