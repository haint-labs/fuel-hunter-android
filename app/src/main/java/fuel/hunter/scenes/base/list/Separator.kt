package fuel.hunter.scenes.base.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fuel.hunter.ui.ColorSeparator

@Composable
fun Separator() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorSeparator)
            .height(1.dp)
    )
}