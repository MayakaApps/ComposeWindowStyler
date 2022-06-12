import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mayakapps.compose.windowstyler.ApplyEffect
import com.mayakapps.compose.windowstyler.WindowEffect

@Composable
@Preview
fun App(isDark: Boolean = isSystemInDarkTheme()) {
    MaterialTheme(colors = if (isDark) darkColors() else lightColors()) {
        Column {
            Spacer(Modifier.height(50.dp))
            Row {
                Spacer(Modifier.width(200.dp))

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    elevation = 4.dp,
                    shape = RoundedCornerShape(topStart = 12.dp),
                ) {}
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        ApplyEffect(WindowEffect.Mica(isSystemInDarkTheme()))

        App()
    }
}
