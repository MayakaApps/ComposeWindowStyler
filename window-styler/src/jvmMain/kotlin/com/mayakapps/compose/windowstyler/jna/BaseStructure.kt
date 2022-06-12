package com.mayakapps.compose.windowstyler.jna

import com.sun.jna.Structure

internal open class BaseStructure : Structure(), Structure.ByReference {
    fun dispose() = clear()
}