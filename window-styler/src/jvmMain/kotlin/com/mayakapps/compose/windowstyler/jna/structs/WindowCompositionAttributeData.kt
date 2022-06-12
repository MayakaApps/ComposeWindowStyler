package com.mayakapps.compose.windowstyler.jna.structs

import com.mayakapps.compose.windowstyler.jna.enums.WindowCompositionAttribute
import com.sun.jna.Structure.FieldOrder

@Suppress("unused")
@FieldOrder(
    "attribute",
    "data",
    "sizeOfData",
)
internal class WindowCompositionAttributeData(
    attribute: WindowCompositionAttribute = WindowCompositionAttribute.WCA_UNDEFINED,
    @JvmField var data: AccentPolicy = AccentPolicy(),
) : BaseStructure() {

    @JvmField
    var attribute: Int = attribute.value

    @JvmField
    var sizeOfData: Int = data.size()

    override fun dispose() {
        data.dispose()
        super.dispose()
    }
}