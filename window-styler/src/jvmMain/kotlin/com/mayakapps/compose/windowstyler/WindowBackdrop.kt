package com.mayakapps.compose.windowstyler

import androidx.compose.ui.graphics.Color

/**
 * The type of the window backdrop/background.
 *
 * **Fallback Strategy**
 *
 * In case of unsupported effect the library tries to fall back to the nearest supported effect as follows:
 *
 * [Tabbed] -> [Mica] -> [Acrylic] -> [Transparent]
 *
 * [Aero] is dropped from the fallback as it is much more transparent than [Tabbed] or [Mica] and not customizable as
 * [Acrylic]. If [Tabbed] or [Mica] falls back to [Acrylic] or [Transparent], high alpha is used with white or black
 * color according to `isDarkTheme` to emulate these effects.
 */
sealed interface WindowBackdrop {

    /**
     * This effect provides a simple solid backdrop colored as white or black according to isDarkTheme. This allows the
     * backdrop to blend with the title bar as well. Though its name may imply that the window will be left unchanged,
     * this is not the case as once the transparency is hacked into the window, it can't be reverted.
     */
    object Default : WindowBackdrop

    /**
     * This applies [color] as a solid background which means that any alpha component is ignored and the color is
     * rendered as opaque.
     */
    open class Solid(override val color: Color) : WindowBackdrop, ColorableWindowBackdrop {
        override fun equals(other: Any?): Boolean = equalsImpl(other)
        override fun hashCode(): Int = hashCodeImpl()
    }

    /**
     * Same as [Solid] but allows transparency taking into account the alpha value. If the passed [color] is fully
     * opaque, the alpha is set to 0.5F.
     */
    open class Transparent(color: Color) : WindowBackdrop, ColorableWindowBackdrop {
        // If you really want the color to be fully opaque, just use Solid which is simpler and more stable
        override val color: Color =
            if (color.alpha != 1F) color else color.copy(alpha = 0.5F)

        override fun equals(other: Any?): Boolean = equalsImpl(other)
        override fun hashCode(): Int = hashCodeImpl()

        /**
         * This makes the window fully transparent.
         */
        companion object : Transparent(Color.Transparent)
    }

    /**
     * This applies [Aero](https://en.wikipedia.org/wiki/Windows_Aero) backdrop which is Windows Vista and Windows 7
     * version of blur.
     *
     * This effect doesn't allow any customization.
     */
    object Aero : WindowBackdrop

    /**
     * This applies [Acrylic](https://docs.microsoft.com/en-us/windows/apps/design/style/acrylic) backdrop blended with
     * the supplied [color]. If the backdrop is rendered opaque, double check that [color] has reasonable alpha value.
     *
     * **Supported on Windows 10 version 1803 or greater.**
     */
    open class Acrylic(override val color: Color) : WindowBackdrop, ColorableWindowBackdrop {
        override fun equals(other: Any?): Boolean = equalsImpl(other)
        override fun hashCode(): Int = hashCodeImpl()
    }

    /**
     * This applies [Mica](https://docs.microsoft.com/en-us/windows/apps/design/style/mica) backdrop themed according
     * to `isDarkTheme` value.
     *
     * **Supported on Windows 11 21H2 or greater.**
     */
    object Mica : WindowBackdrop

    /**
     * This applies Tabbed backdrop themed according to `isDarkTheme` value. This is a backdrop that is similar to
     * [Mica] but targeted at tabbed windows.
     *
     * **Supported on Windows 11 22H2 or greater.**
     */
    object Tabbed : WindowBackdrop
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