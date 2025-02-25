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

import com.mayakapps.compose.windowstyler.windows.jna.enums.WindowCompositionAttribute
import com.sun.jna.Structure.FieldOrder

@Suppress("unused")
@FieldOrder(
    "attribute",
    "data",
    "sizeOfData",
)
internal class WindowCompositionAttributeData(
    attribute: WindowCompositionAttribute = WindowCompositionAttribute.WCA_UNDEFINED,
    @JvmField var data: AccentPolicy = AccentPolicy(),
) : BaseStructure() {

    @JvmField
    var attribute: Int = attribute.value

    @JvmField
    var sizeOfData: Int = data.size()

    override fun dispose() {
        data.dispose()
        super.dispose()
    }
}