package com.mayakapps.compose.windowstyler.jna.structs

import com.sun.jna.Structure

@Structure.FieldOrder(
    "leftWidth",
    "rightWidth",
    "topHeight",
    "bottomHeight",
)
internal data class Margins(
    @JvmField var leftWidth: Int = 0,
    @JvmField var rightWidth: Int = 0,
    @JvmField var topHeight: Int = 0,
    @JvmField var bottomHeight: Int = 0,
) : BaseStructure()