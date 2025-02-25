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
internal enum class WindowCompositionAttribute(val value: Int) {
    WCA_UNDEFINED(0),
    WCA_NCRENDERING_ENABLED(1),
    WCA_NCRENDERING_POLICY(2),
    WCA_TRANSITIONS_FORCEDISABLED(3),
    WCA_ALLOW_NCPAINT(4),
    WCA_CAPTION_BUTTON_BOUNDS(5),
    WCA_NONCLIENT_RTL_LAYOUT(6),
    WCA_FORCE_ICONIC_REPRESENTATION(7),
    WCA_EXTENDED_FRAME_BOUNDS(8),
    WCA_HAS_ICONIC_BITMAP(9),
    WCA_THEME_ATTRIBUTES(10),
    WCA_NCRENDERING_EXILED(11),
    WCA_NCADORNMENTINFO(12),
    WCA_EXCLUDED_FROM_LIVEPREVIEW(13),
    WCA_VIDEO_OVERLAY_ACTIVE(14),
    WCA_FORCE_ACTIVEWINDOW_APPEARANCE(15),
    WCA_DISALLOW_PEEK(16),
    WCA_CLOAK(17),
    WCA_CLOAKED(18),
    WCA_ACCENT_POLICY(19),
    WCA_FREEZE_REPRESENTATION(20),
    WCA_EVER_UNCLOAKED(21),
    WCA_VISUAL_OWNER(22),
    WCA_HOLOGRAPHIC(23),
    WCA_EXCLUDED_FROM_DDA(24),
    WCA_PASSIVEUPDATEMODE(25),
    WCA_USEDARKMODECOLORS(26),
    WCA_LAST(27),
}