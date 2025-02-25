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

package com.mayakapps.compose.windowstyler.windows.jna.enums

@Suppress("SpellCheckingInspection", "unused")
internal enum class DwmWindowAttribute(val value: Int) {
    DWMWA_NCRENDERING_ENABLED(0),
    DWMWA_NCRENDERING_POLICY(1),
    DWMWA_TRANSITIONS_FORCEDISABLED(2),
    DWMWA_ALLOW_NCPAINT(3),
    DWMWA_CAPTION_BUTTON_BOUNDS(4),
    DWMWA_NONCLIENT_RTL_LAYOUT(5),
    DWMWA_FORCE_ICONIC_REPRESENTATION(6),
    DWMWA_FLIP3D_POLICY(7),
    DWMWA_EXTENDED_FRAME_BOUNDS(8),
    DWMWA_HAS_ICONIC_BITMAP(9),
    DWMWA_DISALLOW_PEEK(10),
    DWMWA_EXCLUDED_FROM_PEEK(11),
    DWMWA_CLOAK(12),
    DWMWA_CLOAKED(13),
    DWMWA_FREEZE_REPRESENTATION(14),
    DWMWA_PASSIVE_UPDATE_MODE(15),
    DWMWA_USE_HOSTBACKDROPBRUSH(16),
    DWMWA_USE_IMMERSIVE_DARK_MODE_BEFORE_20H1(19),
    DWMWA_USE_IMMERSIVE_DARK_MODE(20),
    DWMWA_WINDOW_CORNER_PREFERENCE(33),
    DWMWA_BORDER_COLOR(34),
    DWMWA_CAPTION_COLOR(35),
    DWMWA_TEXT_COLOR(36),
    DWMWA_VISIBLE_FRAME_BORDER_THICKNESS(37),
    DWMWA_SYSTEMBACKDROP_TYPE(38),
    DWMWA_LAST(39),
    DWMWA_MICA_EFFECT(1029),
}