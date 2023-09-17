package com.mayakapps.compose.windowstyler.windows

import com.mayakapps.compose.windowstyler.windows.jna.Dwm
import com.mayakapps.compose.windowstyler.windows.jna.User32
import com.mayakapps.compose.windowstyler.windows.jna.enums.AccentFlag
import com.mayakapps.compose.windowstyler.windows.jna.enums.AccentState
import com.mayakapps.compose.windowstyler.windows.jna.enums.DwmSystemBackdrop
import com.mayakapps.compose.windowstyler.windows.jna.enums.DwmWindowAttribute
import com.sun.jna.platform.win32.WinDef

internal class WindowsBackdropApis private constructor(private val hwnd: WinDef.HWND) {
    private var isSystemBackdropSet = false
    private var isMicaEnabled = false
    private var isAccentPolicySet = false
    private var isSheetOfGlassApplied = false

    companion object {
        /**
         * Instantiate [WindowsBackdropApis] for the given window and install it.
         */
        fun install(hwnd: WinDef.HWND) = WindowsBackdropApis(hwnd)
    }

    fun setSystemBackdrop(systemBackdrop: DwmSystemBackdrop) {
        createSheetOfGlassEffect()
        if (Dwm.setSystemBackdrop(hwnd, systemBackdrop)) {
            isSystemBackdropSet = systemBackdrop == DwmSystemBackdrop.DWMSBT_DISABLE
            if (isSystemBackdropSet) resetAccentPolicy()
        }
    }

    fun setMicaEffectEnabled(enabled: Boolean) {
        createSheetOfGlassEffect()
        if (Dwm.setWindowAttribute(hwnd, DwmWindowAttribute.DWMWA_MICA_EFFECT, enabled)) {
            isMicaEnabled = enabled
            if (isMicaEnabled) resetAccentPolicy()
        }
    }

    fun setAccentPolicy(
        accentState: AccentState = AccentState.ACCENT_DISABLED,
        accentFlags: Set<AccentFlag> = emptySet(),
        color: Int = 0,
        animationId: Int = 0,
    ) {
        if (User32.setAccentPolicy(hwnd, accentState, accentFlags, color, animationId)) {
            isAccentPolicySet = accentState != AccentState.ACCENT_DISABLED
            if (isAccentPolicySet) {
                resetSystemBackdrop()
                resetMicaEffectEnabled()
                resetWindowFrame()
            }
        }
    }

    fun createSheetOfGlassEffect() {
        if (!isSheetOfGlassApplied && Dwm.extendFrameIntoClientArea(hwnd, -1)) isSheetOfGlassApplied = true
    }


    fun resetSystemBackdrop() {
        if (isSystemBackdropSet) setSystemBackdrop(DwmSystemBackdrop.DWMSBT_DISABLE)
    }

    fun resetMicaEffectEnabled() {
        if (isMicaEnabled) setMicaEffectEnabled(false)
    }

    fun resetAccentPolicy() {
        if (isAccentPolicySet) setAccentPolicy(AccentState.ACCENT_DISABLED)
    }

    fun resetWindowFrame() {
        // At least one margin should be non-negative in order to show the DWM
        // window shadow created by handling [WM_NCCALCSIZE].
        //
        // Matching value with bitsdojo_window.
        // https://github.com/bitsdojo/bitsdojo_window/blob/adad0cd40be3d3e12df11d864f18a96a2d0fb4fb/bitsdojo_window_windows/windows/bitsdojo_window.cpp#L149
        if (isSheetOfGlassApplied && Dwm.extendFrameIntoClientArea(hwnd, 0, 0, 1, 0)) {
            isSheetOfGlassApplied = false
        }
    }
}