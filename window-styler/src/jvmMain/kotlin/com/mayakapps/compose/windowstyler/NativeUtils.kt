package com.mayakapps.compose.windowstyler

import androidx.compose.ui.awt.ComposeWindow
import com.mayakapps.compose.windowstyler.jna.Nt
import com.mayakapps.compose.windowstyler.jna.enums.DwmSystemBackdrop
import com.mayakapps.compose.windowstyler.jna.structs.OsVersionInfo
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinDef
import javax.swing.JFrame

internal val ComposeWindow.windowHWND
    get() = WinDef.HWND(Pointer(windowHandle))

internal val JFrame.windowHWND
    get() = WinDef.HWND(Native.getWindowPointer(this))

internal val windowsBuild by lazy {
    val osVersionInfo = OsVersionInfo()
    Nt.RtlGetVersion(osVersionInfo)
    osVersionInfo.buildNumber
}

internal fun WindowBackdrop.toDwmSystemBackdrop(): DwmSystemBackdrop =
    when (this) {
        is WindowBackdrop.Mica -> DwmSystemBackdrop.DWMSBT_MAINWINDOW
        is WindowBackdrop.Acrylic -> DwmSystemBackdrop.DWMSBT_TRANSIENTWINDOW
        is WindowBackdrop.Tabbed -> DwmSystemBackdrop.DWMSBT_TABBEDWINDOW
        else -> DwmSystemBackdrop.DWMSBT_DISABLE
    }
