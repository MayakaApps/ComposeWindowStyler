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

package com.mayakapps.compose.windowstyler.windows.jna.structs

import com.mayakapps.compose.windowstyler.windows.jna.enums.AccentFlag
import com.mayakapps.compose.windowstyler.windows.jna.enums.AccentState
import com.mayakapps.compose.windowstyler.windows.jna.orOf
import com.sun.jna.Structure.FieldOrder

@Suppress("unused")
@FieldOrder(
    "accentState",
    "accentFlags",
    "color",
    "animationId",
)
internal class AccentPolicy(
    accentState: AccentState = AccentState.ACCENT_DISABLED,
    accentFlags: Set<AccentFlag> = emptySet(),
    @JvmField var color: Int = 0,
    @JvmField var animationId: Int = 0,
) : BaseStructure() {

    @JvmField
    var accentState: Int = accentState.value

    @JvmField
    var accentFlags: Int = accentFlags.orOf { it.value }
}