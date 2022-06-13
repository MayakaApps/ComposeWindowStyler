package com.mayakapps.compose.windowstyler

import androidx.compose.ui.graphics.Color

sealed interface WindowBackdrop {

    val supportedSince: Int

    object Default : WindowBackdrop {
        override val supportedSince: Int = 0 // Unknown but old
    }

    data class Solid(override val color: Color) : WindowBackdrop, ColorableWindowBackdrop {
        override val supportedSince: Int = 0 // Unknown but old
    }

    data class Transparent(override val color: Color) : WindowBackdrop, ColorableWindowBackdrop {
        override val supportedSince: Int = 0 // Unknown but old
    }

    object Aero : WindowBackdrop {
        override val supportedSince: Int = 0 // Unknown but old
    }

    data class Acrylic(override val color: Color) : WindowBackdrop, ColorableWindowBackdrop {
        override val supportedSince: Int = 170630
    }

    object Mica : WindowBackdrop {
        override val supportedSince: Int = 22000
    }

    object Tabbed : WindowBackdrop {
        override val supportedSince: Int = 22523
    }
}

internal sealed interface ColorableWindowBackdrop {
    val color: Color
}