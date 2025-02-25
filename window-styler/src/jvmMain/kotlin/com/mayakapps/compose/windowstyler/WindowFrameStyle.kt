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

import androidx.compose.ui.graphics.Color

/**
 * Styles for the window frame which includes the title bar and window border.
 *
 * All these styles are only supported on Windows 11 or greater and has no effect on other OSes.
 *
 * @property borderColor Specifies the color of the window border that is running around the window if the window is
 * decorated. This property doesn't support transparency.
 * @property titleBarColor Specifies the color of the window title bar (caption bar) if the window is decorated. This
 * property doesn't support transparency.
 * @property captionColor Specifies the color of the window caption (title) text if the window is decorated. This
 * property doesn't support transparency.
 * @property cornerPreference Specifies the shape of the corners you want. For example, you can use this property to
 * avoid rounded corners in a decorated window or get the corners rounded in an undecorated window.
 */
data class WindowFrameStyle(
    val borderColor: Color = Color.Unspecified,
    val titleBarColor: Color = Color.Unspecified,
    val captionColor: Color = Color.Unspecified,
    val cornerPreference: WindowCornerPreference = WindowCornerPreference.DEFAULT
)

/**
 * The preferred corner shape of the window.
 */
enum class WindowCornerPreference {
    DEFAULT,
    NOT_ROUNDED,
    ROUNDED,
    SMALL_ROUNDED,
}