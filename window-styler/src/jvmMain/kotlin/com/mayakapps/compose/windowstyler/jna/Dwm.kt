package com.mayakapps.compose.windowstyler.jna

import com.mayakapps.compose.windowstyler.jna.enums.DwmWindowAttribute
import com.mayakapps.compose.windowstyler.jna.structs.Margins
import com.sun.jna.Native
import com.sun.jna.PointerType
import com.sun.jna.platform.win32.WinDef.HWND
import com.sun.jna.platform.win32.WinNT.HRESULT
import com.sun.jna.win32.StdCallLibrary
import com.sun.jna.win32.W32APIOptions

internal val Dwm: DwmApi by lazy {
    @Suppress("SpellCheckingInspection")
    Native.load("dwmapi", DwmApi::class.java, W32APIOptions.DEFAULT_OPTIONS)
}

@Suppress("FunctionName")
internal interface DwmApi : StdCallLibrary {
    fun DwmExtendFrameIntoClientArea(hwnd: HWND, margins: Margins): HRESULT
    fun DwmSetWindowAttribute(hwnd: HWND, attribute: Int, attributeValue: PointerType?, valueSize: Int): HRESULT
}

@Suppress("FunctionName")
internal fun DwmApi.DwmSetWindowAttribute(
    hwnd: HWND,
    attribute: DwmWindowAttribute,
    attributeValue: PointerType?,
    valueSize: Int,
): HRESULT = DwmSetWindowAttribute(hwnd, attribute.value, attributeValue, valueSize)