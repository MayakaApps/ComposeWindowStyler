import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mayakapps.compose.windowstyler.WindowBackdrop
import com.mayakapps.compose.windowstyler.WindowStyle

@Composable
@Preview
fun App() {
    Button(onClick = {}) {
        Text("Button")
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose Window Styler Demo",
    ) {
        WindowStyle(
            isDarkTheme = isSystemInDarkTheme(),
            backdropType = WindowBackdrop.Mica,
            manageTitlebar = true
        )

        MaterialTheme {
            App()
        }
    }
}
