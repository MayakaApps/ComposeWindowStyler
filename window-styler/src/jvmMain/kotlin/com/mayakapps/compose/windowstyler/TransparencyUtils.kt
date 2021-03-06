package com.mayakapps.compose.windowstyler

import androidx.compose.ui.awt.ComposeWindow
import org.jetbrains.skiko.SkiaLayer
import java.awt.*
import javax.swing.JComponent
import javax.swing.JDialog
import javax.swing.JWindow

internal fun ComposeWindow.setComposeLayerTransparency(isTransparent: Boolean) {
    skiaLayer.transparency = isTransparent
}

internal fun Window.hackContentPane() {
    val oldContentPane = contentPane ?: return

    // Create hacked content pane the same way of AWT
    val newContentPane: JComponent = HackedContentPane()
    newContentPane.name = "$name.contentPane"
    newContentPane.layout = object : BorderLayout() {
        override fun addLayoutComponent(comp: Component, constraints: Any?) {
            super.addLayoutComponent(comp, constraints ?: CENTER)
        }
    }

    newContentPane.background = Color(0, 0, 0, 0)

    oldContentPane.components.forEach { newContentPane.add(it) }

    contentPane = newContentPane
}


internal val ComposeWindow.skiaLayer: SkiaLayer
    get() {
        val delegate = delegateField.get(this)
        val layer = getLayerMethod.invoke(delegate)
        return getComponentMethod.invoke(layer) as SkiaLayer
    }

internal val Window.isTransparent
    get() = when (this) {
        is ComposeWindow -> skiaLayer.transparency
        else -> background.alpha != 255
    }

internal val Window.isUndecorated
    get() = when (this) {
        is Frame -> isUndecorated
        is JDialog -> isUndecorated
        is JWindow -> true
        else -> false
    }


private val delegateField by lazy {
    ComposeWindow::class.java.getDeclaredField("delegate").apply { isAccessible = true }
}

private val getLayerMethod by lazy {
    delegateField.type.getDeclaredMethod("getLayer").apply { isAccessible = true }
}

private val getComponentMethod by lazy {
    getLayerMethod.returnType.getDeclaredMethod("getComponent")
}