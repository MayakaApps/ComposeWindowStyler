package com.mayakapps.compose.windowstyler.jna

import com.sun.jna.Native
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.win32.StdCallLibrary
import com.sun.jna.win32.W32APIOptions

internal val User32: User32Api by lazy {
    Native.load("user32", User32Api::class.java, W32APIOptions.DEFAULT_OPTIONS)
}

@Suppress("FunctionName")
internal interface User32Api : StdCallLibrary {
    fun SetWindowCompositionAttribute(hwnd: WinDef.HWND, attributeData: WindowCompositionAttributeData): Boolean
}