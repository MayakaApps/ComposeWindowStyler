import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> RadioGroup(title: String, options: List<Pair<T, String>>, selected: T, onSelectedChange: (T) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.h6,
        )

        Spacer(Modifier.height(8.dp))

        LazyVerticalGrid(columns = GridCells.Adaptive(200.dp)) {
            items(options) { (option, name) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = option == selected,
                        onClick = { onSelectedChange(option) },
                    )

                    Text(name)
                }
            }
        }
    }
}