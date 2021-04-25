package fuel.hunter.scenes.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fuel.hunter.scenes.base.page.LocationIcon
import fuel.hunter.ui.ColorPrimary
import fuel.hunter.view.decorations.glow
import fuel.hunter.view.decorations.roundIndication

class GlowingToolbarState(
    initial: Color,
    private val maxAlpha: Float,
) {
    private var _color by mutableStateOf(initial)

    var color: Color
        get() = _color
        set(value) {
            _color = value
        }

    var alpha: Float
        get() = _color.alpha
        set(value) {
            _color = _color.copy(
                alpha = value.coerceIn(0f, maxAlpha) / maxAlpha
            )
        }
}

@Composable
fun rememberToolbarState(
    color: Color = Color.Black,
    maxAlpha: Float = 200f,
): GlowingToolbarState {
    return rememberSaveable(
        color,
        saver = Saver(
            save = { color.value to maxAlpha },
            restore = { (value, alpha) ->
                GlowingToolbarState(Color(value), alpha)
            }
        ),
        init = { GlowingToolbarState(color, maxAlpha) }
    )
}

@Composable
fun Title(
    modifier: Modifier = Modifier,
    text: String = "",
) {
    Text(
        text = text,
        color = ColorPrimary,
        fontSize = 20.sp,
        modifier = modifier
            .wrapContentSize()
    )
}

@Composable
fun GlowingToolbar(
    text: String = "Toolbar",
    navigationIcon: @Composable (BoxScope.() -> Unit)? = null,
    onNavigationClick: () -> Unit = {},
    toolbarState: GlowingToolbarState = rememberToolbarState(),
) {
    GlowingToolbar(
        title = {
            Title(
                text = text,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
        },
        navigationIcon = navigationIcon,
        onNavigationClick = onNavigationClick,
        toolbarState = toolbarState,
    )
}

@Composable
fun GlowingToolbar(
    title: @Composable RowScope.() -> Unit,
    navigationIcon: @Composable (BoxScope.() -> Unit)? = null,
    onNavigationClick: () -> Unit = {},
    toolbarState: GlowingToolbarState = rememberToolbarState(),
) {
    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.Center)
        ) {
            navigationIcon?.let {
                Box(
                    content = { it() },
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = roundIndication(ColorPrimary.copy(alpha = 0.3f)),
                            onClick = onNavigationClick,
                        ),
                )
            }

            title()
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .glow(
                    color = toolbarState.color,
                    strokeWidth = 1.dp,
                    radius = 2.dp,
                    path = {
                        Path().apply {
                            addRect(Rect(Offset(-1f, 0f), Offset(size.width, size.height)))
                        }
                    },
                )
        )
    }
}

@Preview
@Composable
fun PreviewToolbar() {
    Box(modifier = Modifier.size(256.dp)) {
        GlowingToolbar()
    }
}
