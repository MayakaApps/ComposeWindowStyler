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
    Window(onCloseRequest = ::exitApplication) {
        var isDarkTheme by remember { mutableStateOf(false) }
        var backdropType by remember { mutableStateOf<WindowBackdrop>(WindowBackdrop.Default) }

        WindowStyle(isDarkTheme = isDarkTheme, backdropType = backdropType)

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
    WindowBackdrop.Aero to "Aero",
    WindowBackdrop.Acrylic(Color.Magenta.copy(alpha = 0.25F)) to "Acrylic Magenta",
    WindowBackdrop.Acrylic(Color.Cyan.copy(alpha = 0.25F)) to "Acrylic Cyan",
    WindowBackdrop.Mica to "Mica",
    WindowBackdrop.Tabbed to "Tabbed",
)