package com.mayakapps.compose.windowstyler.jna.structs

import com.sun.jna.Pointer
import com.sun.jna.Structure.FieldOrder
import com.sun.jna.platform.win32.WinDef.ULONG

@Suppress("unused")
@FieldOrder(
    "osVersionInfoSize",
    "majorVersion",
    "minorVersion",
    "buildNumber",
    "platformId",
    "csdVersion",
)
internal class OsVersionInfo(
    @JvmField var majorVersion: Int = 0,
    @JvmField var minorVersion: Int = 0,
    @JvmField var buildNumber: Int = 0,
    @JvmField var platformId: Int = 0,
) : BaseStructure() {

    @JvmField
    var osVersionInfoSize: Int = (ULONG.SIZE * 5) + 4

    @JvmField
    var csdVersion: Pointer? = null
}