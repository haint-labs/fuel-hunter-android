package fuel.hunter.scenes.base

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun BaseLayout(
    toolbar: @Composable ColumnScope.() -> Unit,
    children: @Composable BoxScope.() -> Unit,
) {
    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart,
        ) {
            Column {
                toolbar()
                Box(
                    modifier = Modifier.fillMaxSize(),
                    content = children
                )
            }
        }
    }
}