package com.mayakapps.compose.windowstyler.windows.jna

import com.mayakapps.compose.windowstyler.windows.jna.enums.AccentFlag
import com.mayakapps.compose.windowstyler.windows.jna.enums.AccentState
import com.mayakapps.compose.windowstyler.windows.jna.enums.WindowCompositionAttribute
import com.mayakapps.compose.windowstyler.windows.jna.structs.AccentPolicy
import com.mayakapps.compose.windowstyler.windows.jna.structs.WindowCompositionAttributeData
import com.sun.jna.Native
import com.sun.jna.Structure
import com.sun.jna.platform.win32.BaseTSD.LONG_PTR
import com.sun.jna.platform.win32.WinDef.HWND
import com.sun.jna.platform.win32.WinDef.LPARAM
import com.sun.jna.platform.win32.WinDef.LRESULT
import com.sun.jna.platform.win32.WinDef.RECT
import com.sun.jna.platform.win32.WinDef.WPARAM
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.platform.win32.WinUser.WindowProc
import com.sun.jna.win32.StdCallLibrary
import com.sun.jna.win32.W32APIOptions


internal object User32 {

    fun setAccentPolicy(
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

        val isSuccess = setWindowCompositionAttribute(hwnd, data)

        data.dispose()
        return isSuccess
    }

    private fun setWindowCompositionAttribute(
        hwnd: HWND,
        attributeData: WindowCompositionAttributeData,
    ): Boolean {
        Native.setLastError(0)

        val isSuccess = User32Impl.SetWindowCompositionAttribute(hwnd, attributeData)

        if (!isSuccess) println("SetWindowCompositionAttribute(${attributeData.attribute}) failed with last error ${Native.getLastError()}")
        return isSuccess
    }

    fun setWindowLongAttr(hwnd: HWND, index: Int, long: LONG_PTR) =
        User32Impl.SetWindowLongPtr(hwnd, index, long)

    fun getWindowLongAttr(hwnd: HWND, index: Int) = User32Impl.GetWindowLongPtr(hwnd, index)

    fun setWindowProc(hwnd: HWND, withWndProc: LONG_PTR) = setWindowLongAttr(hwnd, WinUser.GWL_WNDPROC, withWndProc)
    fun setWindowProc(hwnd: HWND, withWndProc: WindowProc) =
        User32Impl.SetWindowLongPtr(hwnd, WinUser.GWL_WNDPROC, withWndProc)

    fun callWindowProc(
        defWndProc: LONG_PTR,
        hwnd: HWND,
        uMsg: Int,
        wparam: WPARAM,
        lparam: LPARAM,
    ): LRESULT =
        User32Impl.CallWindowProc(defWndProc, hwnd, uMsg, wparam, lparam)

    fun setWindowPos(
        hwnd: HWND,
        hWndInsertAfter: HWND? = null,
        x: Int = 0,
        y: Int = 0,
        cx: Int = 0,
        cy: Int = 0,
        uFlags: Int = 0,
    ): Boolean = User32Impl.SetWindowPos(hwnd, hWndInsertAfter, x, y, cx, cy, uFlags)

    fun getWindowRect(hwnd: HWND) = RECT().also { User32Impl.GetWindowRect(hwnd, it) }
}

// See https://stackoverflow.com/q/62240901
@Suppress("unused")
@Structure.FieldOrder(
    "cxLeftWidth",
    "cxRightWidth",
    "cyTopHeight",
    "cyBottomHeight"
)
private object User32Impl : User32Api by Native.load("user32", User32Api::class.java, W32APIOptions.DEFAULT_OPTIONS)


@Suppress("FunctionName")
private interface User32Api : StdCallLibrary {
    fun SetWindowCompositionAttribute(hwnd: HWND, attributeData: WindowCompositionAttributeData): Boolean
    fun SetWindowPos(hWnd: HWND, hWndInsertAfter: HWND?, x: Int, y: Int, cx: Int, cy: Int, uFlags: Int): Boolean
    fun GetWindowRect(hWnd: HWND, rect: RECT)
    fun SetWindowLongPtr(hWnd: HWND, nIndex: Int, wndProc: WindowProc): LONG_PTR
    fun SetWindowLongPtr(hWnd: HWND, nIndex: Int, wndProc: LONG_PTR): LONG_PTR
    fun GetWindowLongPtr(hWnd: HWND, nIndex: Int): LONG_PTR
    fun CallWindowProc(proc: LONG_PTR, hWnd: HWND, uMsg: Int, uParam: WPARAM, lParam: LPARAM): LRESULT
}