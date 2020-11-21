package fuel.hunter.scenes.base.list

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fuel.hunter.ui.ColorPrimary
import fuel.hunter.ui.ListItemTitleTextStyle
import fuel.hunter.view.decorations.glow

@Composable
fun ListItem(
    title: String,
    subtitle: String? = null,
    icon: @Composable (() -> Unit)? = null,
    action: @Composable () -> Unit = {},
    listItemType: ListItemType = Single,
) {
    Column(
        modifier = Modifier
            .clipToBounds()
            .glow(
                color = ColorPrimary.copy(alpha = 102f / 255f),
                strokeWidth = 1.dp,
                radius = 2.dp,
            ) {
                listItemType.glow(size, 4.dp.toPx(), 8.dp.toPx())
            }
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(listItemType.padding)
        ) {
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .width(33.dp)
                        .height(33.dp)
                ) {
                    icon()
                }
            }

            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .weight(1f),
            ) {
                Text(
                    text = title,
                    color = ColorPrimary,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    style = ListItemTitleTextStyle,
                )

                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        color = Color.Gray,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            action()
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 4.dp)
        ) {
            listItemType.separator()
        }
    }
}