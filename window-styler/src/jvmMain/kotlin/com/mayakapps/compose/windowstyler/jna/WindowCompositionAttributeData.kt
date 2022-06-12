package com.mayakapps.compose.windowstyler.jna

import com.sun.jna.Structure.FieldOrder

@FieldOrder(
    "attribute",
    "data",
    "sizeOfData",
)
internal data class WindowCompositionAttributeData(
    @JvmField var attribute: Int = 0,
    @JvmField var data: AccentPolicy = AccentPolicy(),
    @JvmField var sizeOfData: Int = 0,
) : BaseStructure()

@Suppress("SpellCheckingInspection", "unused")
internal object WindowCompositionAttribute {
    const val WCA_UNDEFINED = 0
    const val WCA_NCRENDERING_ENABLED = 1
    const val WCA_NCRENDERING_POLICY = 2
    const val WCA_TRANSITIONS_FORCEDISABLED = 3
    const val WCA_ALLOW_NCPAINT = 4
    const val WCA_CAPTION_BUTTON_BOUNDS = 5
    const val WCA_NONCLIENT_RTL_LAYOUT = 6
    const val WCA_FORCE_ICONIC_REPRESENTATION = 7
    const val WCA_EXTENDED_FRAME_BOUNDS = 8
    const val WCA_HAS_ICONIC_BITMAP = 9
    const val WCA_THEME_ATTRIBUTES = 10
    const val WCA_NCRENDERING_EXILED = 11
    const val WCA_NCADORNMENTINFO = 12
    const val WCA_EXCLUDED_FROM_LIVEPREVIEW = 13
    const val WCA_VIDEO_OVERLAY_ACTIVE = 14
    const val WCA_FORCE_ACTIVEWINDOW_APPEARANCE = 15
    const val WCA_DISALLOW_PEEK = 16
    const val WCA_CLOAK = 17
    const val WCA_CLOAKED = 18
    const val WCA_ACCENT_POLICY = 19
    const val WCA_FREEZE_REPRESENTATION = 20
    const val WCA_EVER_UNCLOAKED = 21
    const val WCA_VISUAL_OWNER = 22
    const val WCA_HOLOGRAPHIC = 23
    const val WCA_EXCLUDED_FROM_DDA = 24
    const val WCA_PASSIVEUPDATEMODE = 25
    const val WCA_USEDARKMODECOLORS = 26
    const val WCA_LAST = 27
}