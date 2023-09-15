package com.mayakapps.compose.windowstyler.windows

import com.mayakapps.compose.windowstyler.windows.jna.Dwm
import com.mayakapps.compose.windowstyler.windows.jna.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser

private const val WM_NCCALCSIZE = 0x0083
private const val WM_NCHITTEST = 0x0084

class CustomDecorationWindowProc private constructor(private val hwnd: WinDef.HWND) : WinUser.WindowProc {
    private val defWndProc = User32.setWindowProc(hwnd, this)

    override fun callback(hWnd: WinDef.HWND, uMsg: Int, wParam: WinDef.WPARAM, lParam: WinDef.LPARAM): WinDef.LRESULT {
        if (Dwm.callDefaultWindowHitProc(hwnd, uMsg, wParam, lParam)) {
            return WinDef.LRESULT(0)
        }

        return when (uMsg) {
            WM_NCCALCSIZE -> {
                WinDef.LRESULT(0)
            }

            WM_NCHITTEST -> {
                User32.callWindowProc(defWndProc, hWnd, uMsg, wParam, lParam)
            }

            WinUser.WM_DESTROY -> {
                User32.setWindowProc(hWnd, defWndProc)
                WinDef.LRESULT(-1)
            }

            else -> {
                User32.callWindowProc(defWndProc, hWnd, uMsg, wParam, lParam)
            }
        }
    }

    companion object {
        /**
         * Installs a [CustomDecorationWindowProc] for the given window.
         *
         * @param hwnd The window handle.
         */
        fun install(hwnd: WinDef.HWND) = CustomDecorationWindowProc(hwnd)
    }
}