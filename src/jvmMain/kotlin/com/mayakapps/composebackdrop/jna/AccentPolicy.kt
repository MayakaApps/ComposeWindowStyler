package com.mayakapps.composebackdrop.jna

import com.sun.jna.Structure.FieldOrder

@FieldOrder(
    "accentState",
    "accentFlags",
    "gradientColor",
    "animationId",
)
internal data class AccentPolicy(
    @JvmField var accentState: Int = 0,
    @JvmField var accentFlags: Int = 0,
    @JvmField var gradientColor: Int = 0,
    @JvmField var animationId: Int = 0,
) : BaseStructure()

@Suppress("SpellCheckingInspection", "unused")
internal object AccentState {
    const val ACCENT_DISABLED = 0
    const val ACCENT_ENABLE_GRADIENT = 1
    const val ACCENT_ENABLE_TRANSPARENTGRADIENT = 2
    const val ACCENT_ENABLE_BLURBEHIND = 3
    const val ACCENT_ENABLE_ACRYLICBLURBEHIND = 4
    const val ACCENT_ENABLE_HOSTBACKDROP = 5
    const val ACCENT_INVALID_STATE = 6
}