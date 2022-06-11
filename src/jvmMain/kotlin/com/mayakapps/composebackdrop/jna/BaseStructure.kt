package com.mayakapps.composebackdrop.jna

import com.sun.jna.Structure

internal open class BaseStructure : Structure(), Structure.ByReference {
    fun dispose() = clear()
}