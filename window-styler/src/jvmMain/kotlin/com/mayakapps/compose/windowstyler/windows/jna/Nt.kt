package com.mayakapps.compose.windowstyler.windows.jna

import com.mayakapps.compose.windowstyler.windows.jna.structs.OsVersionInfo
import com.sun.jna.Native
import com.sun.jna.win32.StdCallLibrary
import com.sun.jna.win32.W32APIOptions

internal object Nt {
    fun getVersion() = OsVersionInfo().also { NtImpl.RtlGetVersion(it) }
}

@Suppress("SpellCheckingInspection")
private object NtImpl : NtApi by Native.load("Ntdll", NtApi::class.java, W32APIOptions.DEFAULT_OPTIONS)

@Suppress("FunctionName")
private interface NtApi : StdCallLibrary {
    fun RtlGetVersion(osVersionInfo: OsVersionInfo): Int
}