/*
 * Copyright 2022-2025 MayakaApps
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mayakapps.compose.windowstyler

import java.awt.AlphaComposite
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel

internal class HackedContentPane : JPanel() {

    override fun paint(g: Graphics) {
        if (background.alpha != 255) {
            val gg = g.create()
            try {
                if (gg is Graphics2D) {
                    gg.setColor(background)
                    gg.composite = AlphaComposite.getInstance(AlphaComposite.SRC)
                    gg.fillRect(0, 0, width, height)
                }
            } finally {
                gg.dispose()
            }
        }

        super.paint(g)
    }
}