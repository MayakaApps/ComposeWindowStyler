package com.mayakapps.compose.windowstyler

import androidx.compose.ui.graphics.Color

sealed interface WindowBackdrop {

    val supportedSince: Int

    object Default : WindowBackdrop {
        override val supportedSince: Int = 0 // Unknown but old
    }

    open class Solid(override val color: Color) : WindowBackdrop, ColorableWindowBackdrop {
        override val supportedSince: Int = 0 // Unknown but old

        override fun equals(other: Any?): Boolean = equalsImpl(other)
        override fun hashCode(): Int = hashCodeImpl()
    }

    open class Transparent(color: Color) : WindowBackdrop, ColorableWindowBackdrop {
        // If you really want the color to be fully opaque, just use Solid which is simpler and more stable
        override val color: Color =
            if (color.alpha != 1F) color else color.copy(alpha = 0.5F)

        override val supportedSince: Int = 0 // Unknown but old

        override fun equals(other: Any?): Boolean = equalsImpl(other)
        override fun hashCode(): Int = hashCodeImpl()

        companion object : Transparent(Color.Transparent)
    }

    object Aero : WindowBackdrop {
        override val supportedSince: Int = 0 // Unknown but old
    }

    open class Acrylic(override val color: Color) : WindowBackdrop, ColorableWindowBackdrop {
        override val supportedSince: Int = 17063

        override fun equals(other: Any?): Boolean = equalsImpl(other)
        override fun hashCode(): Int = hashCodeImpl()
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

    fun equalsImpl(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ColorableWindowBackdrop

        return color == other.color
    }

    fun hashCodeImpl(): Int = color.hashCode()
}