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

package com.mayakapps.compose.windowstyler.windows.jna

import com.mayakapps.compose.windowstyler.windows.jna.enums.DwmSystemBackdrop
import com.mayakapps.compose.windowstyler.windows.jna.enums.DwmWindowAttribute
import com.mayakapps.compose.windowstyler.windows.jna.enums.DwmWindowCornerPreference
import com.mayakapps.compose.windowstyler.windows.jna.structs.Margins
import com.sun.jna.Native
import com.sun.jna.PointerType
import com.sun.jna.platform.win32.W32Errors
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinDef.HWND
import com.sun.jna.platform.win32.WinNT.HRESULT
import com.sun.jna.ptr.IntByReference
import com.sun.jna.win32.StdCallLibrary
import com.sun.jna.win32.W32APIOptions

internal object Dwm {
    fun extendFrameIntoClientArea(hwnd: HWND, margin: Int = 0) =
        extendFrameIntoClientArea(hwnd, margin, margin, margin, margin)

    fun extendFrameIntoClientArea(
        hwnd: HWND,
        leftWidth: Int = 0,
        rightWidth: Int = 0,
        topHeight: Int = 0,
        bottomHeight: Int = 0,
    ): Boolean {
        val margins = Margins(leftWidth, rightWidth, topHeight, bottomHeight)

        val result = DwmImpl.DwmExtendFrameIntoClientArea(hwnd, margins)
        if (result != W32Errors.S_OK) println("DwmExtendFrameIntoClientArea failed with result $result")

        margins.dispose()
        return result == W32Errors.S_OK
    }


    fun setSystemBackdrop(hwnd: HWND, systemBackdrop: DwmSystemBackdrop): Boolean =
        setWindowAttribute(hwnd, DwmWindowAttribute.DWMWA_SYSTEMBACKDROP_TYPE, systemBackdrop.value)

    fun setWindowCornerPreference(hwnd: HWND, cornerPreference: DwmWindowCornerPreference): Boolean =
        setWindowAttribute(hwnd, DwmWindowAttribute.DWMWA_WINDOW_CORNER_PREFERENCE, cornerPreference.value)

    fun setWindowAttribute(hwnd: HWND, attribute: DwmWindowAttribute, value: Boolean) =
        setWindowAttribute(hwnd, attribute, WinDef.BOOLByReference(WinDef.BOOL(value)), WinDef.BOOL.SIZE)

    fun setWindowAttribute(hwnd: HWND, attribute: DwmWindowAttribute, value: Int) =
        setWindowAttribute(hwnd, attribute, IntByReference(value), INT_SIZE)

    private fun setWindowAttribute(
        hwnd: HWND,
        attribute: DwmWindowAttribute,
        value: PointerType?,
        valueSize: Int,
    ): Boolean {
        val result = DwmImpl.DwmSetWindowAttribute(hwnd, attribute.value, value, valueSize)

        if (result != W32Errors.S_OK) println("DwmSetWindowAttribute(${attribute.name}) failed with result $result")
        return result == W32Errors.S_OK
    }
}

@Suppress("SpellCheckingInspection")
private object DwmImpl : DwmApi by Native.load("dwmapi", DwmApi::class.java, W32APIOptions.DEFAULT_OPTIONS)

@Suppress("FunctionName")
private interface DwmApi : StdCallLibrary {
    fun DwmExtendFrameIntoClientArea(hwnd: HWND, margins: Margins): HRESULT
    fun DwmSetWindowAttribute(hwnd: HWND, attribute: Int, value: PointerType?, valueSize: Int): HRESULT
}