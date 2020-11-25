package fuel.hunter.scenes.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.savedinstancestate.Saver
import androidx.compose.runtime.savedinstancestate.rememberSavedInstanceState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
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
    return rememberSavedInstanceState(
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
    text: String = "",
    modifier: Modifier = Modifier,
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
    navigationIcon: @Composable (() -> Unit)? = null,
    onNavigationClick: () -> Unit = {},
    toolbarState: GlowingToolbarState = rememberToolbarState(),
) {
    GlowingToolbar(
        title = {
            Title(
                text = text,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        },
        navigationIcon = navigationIcon,
        onNavigationClick = onNavigationClick,
        toolbarState = toolbarState,
    )
}

@Composable
fun GlowingToolbar(
    title: @Composable BoxScope.() -> Unit,
    navigationIcon: @Composable (() -> Unit)? = null,
    onNavigationClick: () -> Unit = {},
    toolbarState: GlowingToolbarState = rememberToolbarState(),
) {
    ConstraintLayout(
        modifier = Modifier.wrapContentSize()
    ) {
        val (toolbar, glow) = createRefs()

        TopAppBar(
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            modifier = Modifier
                .constrainAs(toolbar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                navigationIcon?.let {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .preferredSize(32.dp)
                            .align(Alignment.CenterStart)
                            .clickable(
                                onClick = onNavigationClick,
                                indication = roundIndication(ColorPrimary.copy(alpha = 0.3f)),
                            ),
                        alignment = Alignment.Center,
                        children = { it.invoke() },
                    )
                }

                title()
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .constrainAs(glow) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
                .glow(
                    color = toolbarState.color,
                    strokeWidth = 1.dp,
                    radius = 2.dp,
                    path = {
                        Path().apply {
                            addRect(Rect(Offset.Zero, size))
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
