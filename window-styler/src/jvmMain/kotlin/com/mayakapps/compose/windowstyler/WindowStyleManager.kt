package com.mayakapps.compose.windowstyler

import com.mayakapps.compose.windowstyler.windows.WindowsWindowStyleManager
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs
import java.awt.Window

/**
 * Creates a suitable [WindowStyleManager] for [window] or a stub manager if the OS is not supported.
 *
 * The created manager is initialized by the supplied parameters.
 * See [WindowStyleManager.isDarkTheme], [WindowBackdrop], [WindowFrameStyle].
 */
fun WindowStyleManager(
    window: Window,
    isDarkTheme: Boolean = false,
    backdropType: WindowBackdrop = WindowBackdrop.Default,
    frameStyle: WindowFrameStyle = WindowFrameStyle(),
) = when (hostOs) {
    OS.Windows -> WindowsWindowStyleManager(window, isDarkTheme, backdropType, frameStyle)
    else -> StubWindowStyleManager(isDarkTheme, backdropType, frameStyle)
}

/**
 * Style manager which lets you update the style of the provided window using the exposed properties.
 *
 * Only use this manager if you can't use the `@Composable` method [WindowStyle]
 */
interface WindowStyleManager {

    /**
     * This property should match the theming system used in your application. It's effect depends on the used backdrop
     * as follows:
     * * If the [backdropType] is [WindowBackdrop.Default], [WindowBackdrop.Mica] or [WindowBackdrop.Tabbed], it is
     * used to manage the color of the background whether it is light or dark.
     * * Otherwise, it is used to control the color of the title bar of the window white/black.
     */
    var isDarkTheme: Boolean

    /**
     * The type of the window backdrop/background. See [WindowBackdrop] and its implementations.
     */
    var backdropType: WindowBackdrop

    /**
     * The style of the window frame which includes the title bar and window border. See [WindowFrameStyle].
     */
    var frameStyle: WindowFrameStyle
}

internal class StubWindowStyleManager(
    override var isDarkTheme: Boolean,
    override var backdropType: WindowBackdrop,
    override var frameStyle: WindowFrameStyle,
) : WindowStyleManager