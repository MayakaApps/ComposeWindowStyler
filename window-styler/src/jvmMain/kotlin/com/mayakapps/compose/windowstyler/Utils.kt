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

import java.awt.Window
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JWindow

// Try hard to get the contentPane
internal var Window.contentPane
    get() = when (this) {
        is JFrame -> contentPane
        is JDialog -> contentPane
        is JWindow -> contentPane
        else -> null
    }
    set(value) = when (this) {
        is JFrame -> contentPane = value
        is JDialog -> contentPane = value
        is JWindow -> contentPane = value
        else -> throw IllegalStateException()
    }