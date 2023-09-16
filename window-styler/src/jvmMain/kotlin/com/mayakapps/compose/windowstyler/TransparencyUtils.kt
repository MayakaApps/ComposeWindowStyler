package com.mayakapps.compose.windowstyler

import androidx.compose.ui.awt.ComposeWindow
import org.jetbrains.skiko.SkiaLayer
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.Container
import java.awt.Frame
import java.awt.Window
import javax.swing.JComponent
import javax.swing.JDialog
import javax.swing.JWindow

internal fun ComposeWindow.setComposeLayerTransparency(isTransparent: Boolean) {
    findSkiaLayer()?.transparency = isTransparent
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

private fun <T : JComponent> findComponent(
    container: Container,
    klass: Class<T>,
): T? {
    val componentSequence = container.components.asSequence()
    return componentSequence.filter { klass.isInstance(it) }.ifEmpty {
        componentSequence.filterIsInstance<Container>()
            .mapNotNull { findComponent(it, klass) }
    }.map { klass.cast(it) }.firstOrNull()
}

private inline fun <reified T : JComponent> Container.findComponent() = findComponent(this, T::class.java)

fun ComposeWindow.findSkiaLayer(): SkiaLayer? = findComponent<SkiaLayer>()

internal val Window.isTransparent
    get() = when (this) {
        is ComposeWindow -> findSkiaLayer()?.transparency ?: false
        else -> background.alpha != 255
    }

internal val Window.isUndecorated
    get() = when (this) {
        is Frame -> isUndecorated
        is JDialog -> isUndecorated
        is JWindow -> true
        else -> false
    }
