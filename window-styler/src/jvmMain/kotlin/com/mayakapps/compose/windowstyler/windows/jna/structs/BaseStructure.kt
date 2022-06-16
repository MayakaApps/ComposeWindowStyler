package com.mayakapps.compose.windowstyler.windows.jna.structs

import com.sun.jna.Structure

internal open class BaseStructure : Structure(), Structure.ByReference {
    open fun dispose() = clear()
}