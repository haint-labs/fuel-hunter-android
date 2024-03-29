package fuel.hunter.scenes.base.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fuel.hunter.R
import fuel.hunter.ui.ColorPrimary
import fuel.hunter.ui.ListItemTitleTextStyle
import fuel.hunter.ui.PriceTextStyle
import fuel.hunter.view.decorations.glow

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    icon: @Composable (() -> Unit)? = null,
    action: @Composable (() -> Unit)? = null,
    listItemType: ListItemType = Single,
    onClick: () -> Unit = {},
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
            .then(modifier)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(listItemType.padding)
        ) {
            icon?.invoke()

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

                subtitle?.let {
                    Text(
                        text = it,
                        color = Color.Gray,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            action?.invoke()
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 4.dp)
        ) {
            listItemType.separator()
        }
    }
}

@Preview
@Composable
fun ListItemPreview() {
    Box(modifier = Modifier.size(256.dp)) {
        ListItem(
            title = "I am title",
            subtitle = "I am your subtitle",
            icon = {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(33.dp)
                )
            },
            action = {
                Text(
                    text = "1.46",
                    style = PriceTextStyle
                )
            }
        )
    }
}