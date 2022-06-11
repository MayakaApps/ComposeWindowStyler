package com.mayakapps.composebackdrop.jna

import com.sun.jna.Native
import com.sun.jna.PointerType
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT.HRESULT
import com.sun.jna.win32.StdCallLibrary
import com.sun.jna.win32.W32APIOptions

internal val Dwm: DwmApi by lazy {
    @Suppress("SpellCheckingInspection")
    Native.load("dwmapi", DwmApi::class.java, W32APIOptions.DEFAULT_OPTIONS)
}

@Suppress("FunctionName")
internal interface DwmApi : StdCallLibrary {
    fun DwmExtendFrameIntoClientArea(hwnd: WinDef.HWND, margins: Margins): HRESULT
    fun DwmSetWindowAttribute(hwnd: WinDef.HWND, attribute: Int, attributeValue: PointerType?, valueSize: Int): HRESULT
}