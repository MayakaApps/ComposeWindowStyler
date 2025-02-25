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

package com.mayakapps.compose.windowstyler.windows.jna

import com.mayakapps.compose.windowstyler.windows.jna.structs.OsVersionInfo
import com.sun.jna.Native
import com.sun.jna.win32.StdCallLibrary
import com.sun.jna.win32.W32APIOptions

internal object Nt {
    fun getVersion() = OsVersionInfo().also { NtImpl.RtlGetVersion(it) }
}

@Suppress("SpellCheckingInspection")
private object NtImpl : NtApi by Native.load("Ntdll", NtApi::class.java, W32APIOptions.DEFAULT_OPTIONS)

@Suppress("FunctionName")
private interface NtApi : StdCallLibrary {
    fun RtlGetVersion(osVersionInfo: OsVersionInfo): Int
}