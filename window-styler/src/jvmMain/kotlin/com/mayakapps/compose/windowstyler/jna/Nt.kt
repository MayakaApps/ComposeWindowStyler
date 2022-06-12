package com.mayakapps.compose.windowstyler.jna

import com.sun.jna.Native
import com.sun.jna.win32.StdCallLibrary
import com.sun.jna.win32.W32APIOptions

internal val Nt: NtApi by lazy {
    @Suppress("SpellCheckingInspection")
    Native.load("Ntdll", NtApi::class.java, W32APIOptions.DEFAULT_OPTIONS)
}

@Suppress("FunctionName")
internal interface NtApi : StdCallLibrary {
    fun RtlGetVersion(osVersionInfo: OsVersionInfo): Int
}