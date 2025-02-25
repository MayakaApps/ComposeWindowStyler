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

import com.sun.jna.Pointer
import com.sun.jna.Structure.FieldOrder
import com.sun.jna.platform.win32.WinDef.ULONG

@Suppress("unused")
@FieldOrder(
    "osVersionInfoSize",
    "majorVersion",
    "minorVersion",
    "buildNumber",
    "platformId",
    "csdVersion",
)
internal class OsVersionInfo(
    @JvmField var majorVersion: Int = 0,
    @JvmField var minorVersion: Int = 0,
    @JvmField var buildNumber: Int = 0,
    @JvmField var platformId: Int = 0,
) : BaseStructure() {

    @JvmField
    var osVersionInfoSize: Int = (ULONG.SIZE * 5) + 4

    @JvmField
    var csdVersion: Pointer? = null
}