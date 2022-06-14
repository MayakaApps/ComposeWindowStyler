package com.mayakapps.compose.windowstyler

import androidx.compose.ui.awt.ComposeWindow
import com.mayakapps.compose.windowstyler.jna.Nt
import com.mayakapps.compose.windowstyler.jna.enums.AccentState
import com.mayakapps.compose.windowstyler.jna.enums.DwmSystemBackdrop
import com.mayakapps.compose.windowstyler.jna.structs.OsVersionInfo
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinDef
import java.awt.Window

internal val Window.hwnd
    get() =
        if (this is ComposeWindow) WinDef.HWND(Pointer(windowHandle))
        else WinDef.HWND(Native.getWindowPointer(this))

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


internal fun WindowBackdrop.toAccentState(): AccentState =
    when (this) {
        is WindowBackdrop.Default, is WindowBackdrop.Solid -> AccentState.ACCENT_ENABLE_GRADIENT
        is WindowBackdrop.Transparent -> AccentState.ACCENT_ENABLE_TRANSPARENTGRADIENT
        is WindowBackdrop.Aero -> AccentState.ACCENT_ENABLE_BLURBEHIND
        is WindowBackdrop.Acrylic -> AccentState.ACCENT_ENABLE_ACRYLICBLURBEHIND
        else -> AccentState.ACCENT_DISABLED
    }