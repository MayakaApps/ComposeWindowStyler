package com.mayakapps.compose.windowstyler

import com.mayakapps.compose.windowstyler.jna.Dwm
import com.mayakapps.compose.windowstyler.jna.DwmSetWindowAttribute
import com.mayakapps.compose.windowstyler.jna.User32
import com.mayakapps.compose.windowstyler.jna.enums.*
import com.mayakapps.compose.windowstyler.jna.structs.AccentPolicy
import com.mayakapps.compose.windowstyler.jna.structs.Margins
import com.mayakapps.compose.windowstyler.jna.structs.WindowCompositionAttributeData
import com.sun.jna.Native
import com.sun.jna.platform.win32.W32Errors
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinDef.BOOL
import com.sun.jna.platform.win32.WinDef.HWND

internal object Native {

    fun createSheetOfGlassEffect(hwnd: HWND) =
        extendFrameIntoClientArea(hwnd, -1, -1, -1, -1)

    fun restoreWindowFrame(hwnd: HWND): Boolean {
        // At least one margin should be non-negative in order to show the DWM
        // window shadow created by handling [WM_NCCALCSIZE].
        //
        // Matching value with bitsdojo_window.
        // https://github.com/bitsdojo/bitsdojo_window/blob/adad0cd40be3d3e12df11d864f18a96a2d0fb4fb/bitsdojo_window_windows/windows/bitsdojo_window.cpp#L149
        return extendFrameIntoClientArea(hwnd, 0, 0, 1, 0)
    }

    private fun extendFrameIntoClientArea(
        hwnd: HWND,
        leftWidth: Int = 0,
        rightWidth: Int = 0,
        topHeight: Int = 0,
        bottomHeight: Int = 0,
    ): Boolean {
        val margins = Margins(leftWidth, rightWidth, topHeight, bottomHeight)

        val result = Dwm.DwmExtendFrameIntoClientArea(hwnd, margins)
        if (result != W32Errors.S_OK) println("DwmExtendFrameIntoClientArea failed with result $result")

        margins.dispose()
        return result == W32Errors.S_OK
    }


    fun setImmersiveDarkModeEnabled(hwnd: HWND, enabled: Boolean): Boolean {
        val result = Dwm.DwmSetWindowAttribute(
            hwnd,
            if (windowsBuild >= 18985) DwmWindowAttribute.DWMWA_USE_IMMERSIVE_DARK_MODE
            else DwmWindowAttribute.DWMWA_USE_IMMERSIVE_DARK_MODE_BEFORE_20H1,
            WinDef.BOOLByReference(BOOL(enabled)),
            BOOL.SIZE,
        )

        if (result != W32Errors.S_OK) println("DwmSetWindowAttribute(DWMWA_USE_IMMERSIVE_DARK_MODE) failed with result $result")
        return result == W32Errors.S_OK
    }

    fun setSystemBackdrop(hwnd: HWND, systemBackdrop: DwmSystemBackdrop): Boolean {
        val result = Dwm.DwmSetWindowAttribute(
            hwnd,
            DwmWindowAttribute.DWMWA_SYSTEMBACKDROP_TYPE,
            systemBackdrop.intByReference,
            INT_SIZE,
        )

        if (result != W32Errors.S_OK) println("DwmSetWindowAttribute(DWMWA_MICA_EFFECT) failed with result $result")
        return result == W32Errors.S_OK
    }

    fun setMicaEffectEnabled(hwnd: HWND, enabled: Boolean): Boolean {
        val result = Dwm.DwmSetWindowAttribute(
            hwnd,
            DwmWindowAttribute.DWMWA_MICA_EFFECT,
            WinDef.BOOLByReference(BOOL(enabled)),
            BOOL.SIZE,
        )

        if (result != W32Errors.S_OK) println("DwmSetWindowAttribute(DWMWA_MICA_EFFECT) failed with result $result")
        return result == W32Errors.S_OK
    }


    internal fun setAccentPolicy(
        hwnd: HWND,
        accentState: AccentState = AccentState.ACCENT_DISABLED,
        accentFlags: Set<AccentFlag> = emptySet(),
        color: Int = 0,
        animationId: Int = 0,
    ): Boolean {
        val data = WindowCompositionAttributeData(
            WindowCompositionAttribute.WCA_ACCENT_POLICY,
            AccentPolicy(accentState, accentFlags, color, animationId),
        )

        Native.setLastError(0)

        val isSuccess = User32.SetWindowCompositionAttribute(hwnd, data)

        if (!isSuccess) println("SetWindowCompositionAttribute(WCA_ACCENT_POLICY) failed with last error ${Native.getLastError()}")

        data.dispose()
        return isSuccess
    }

    private const val INT_SIZE = 4
}