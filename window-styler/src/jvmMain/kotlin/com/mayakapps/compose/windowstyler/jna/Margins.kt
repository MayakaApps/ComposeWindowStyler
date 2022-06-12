package com.mayakapps.compose.windowstyler.jna

import com.sun.jna.Structure

@Structure.FieldOrder(
    "cxLeftWidth",
    "cxRightWidth",
    "cyTopHeight",
    "cyBottomHeight",
)
internal data class Margins(
    @JvmField var cxLeftWidth: Int = 0,
    @JvmField var cxRightWidth: Int = 0,
    @JvmField var cyTopHeight: Int = 0,
    @JvmField var cyBottomHeight: Int = 0,
) : BaseStructure()