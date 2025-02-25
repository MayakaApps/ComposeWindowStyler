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

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mayakapps.compose.windowstyler.WindowBackdrop
import com.mayakapps.compose.windowstyler.WindowCornerPreference
import com.mayakapps.compose.windowstyler.WindowFrameStyle
import com.mayakapps.compose.windowstyler.WindowStyle

@Composable
@Preview
fun App(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    backdropType: WindowBackdrop,
    onBackdropChange: (WindowBackdrop) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        RadioGroup("Theme", themeOptions, isDarkTheme, onThemeChange)
        Spacer(Modifier.height(50.dp))
        RadioGroup("Backdrop Type", backdropOptions, backdropType, onBackdropChange)
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose Window Styler Demo",
    ) {
        var isDarkTheme by remember { mutableStateOf(false) }
        var backdropType by remember { mutableStateOf<WindowBackdrop>(WindowBackdrop.Default) }

        WindowStyle(
            isDarkTheme = isDarkTheme,
            backdropType = backdropType,
            frameStyle = WindowFrameStyle(cornerPreference = WindowCornerPreference.NOT_ROUNDED),
        )

        MaterialTheme(colors = if (isDarkTheme) darkColors() else lightColors()) {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colors.onBackground) {
                App(
                    isDarkTheme = isDarkTheme,
                    onThemeChange = { isDarkTheme = it },
                    backdropType = backdropType,
                    onBackdropChange = { backdropType = it },
                )
            }
        }
    }
}

val themeOptions = listOf(false to "Light", true to "Dark")
val backdropOptions = listOf(
    WindowBackdrop.Default to "Default",
    WindowBackdrop.Solid(Color.Red) to "Solid Red",
    WindowBackdrop.Solid(Color.Blue) to "Solid Blue",
    WindowBackdrop.Transparent to "Transparent",
    WindowBackdrop.Transparent(Color.Yellow.copy(alpha = 0.25F)) to "Yellow Transparent",
    WindowBackdrop.Aero to "Aero",
    WindowBackdrop.Acrylic(Color.Magenta.copy(alpha = 0.25F)) to "Acrylic Magenta",
    WindowBackdrop.Acrylic(Color.Cyan.copy(alpha = 0.25F)) to "Acrylic Cyan",
    WindowBackdrop.Mica to "Mica",
    WindowBackdrop.Tabbed to "Tabbed",
)