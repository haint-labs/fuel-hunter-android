package fuel.hunter.scenes.base

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import fuel.hunter.R
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
fun GlowingToolbar(
    screenTitle: String = "Toolbar",
    navigationIcon: VectorAsset = vectorResource(id = R.drawable.ic_launcher_foreground),
    toolbarState: GlowingToolbarState = rememberToolbarState(),
    onNavClick: () -> Unit = {},
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
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val (icon, title) = createRefs()

                Box(
                    modifier = Modifier
                        .constrainAs(icon) {
                            centerVerticallyTo(parent)
                            start.linkTo(parent.start)
                        }
                        .padding(4.dp)
                        .clickable(
                            onClick = onNavClick,
                            indication = roundIndication(
                                colorResource(id = R.color.colorPrimary).copy(alpha = 0.3f)
                            )
                        )
                        .padding(4.dp),
                    alignment = Alignment.Center,
                ) {
                    Image(
                        asset = navigationIcon,
                        modifier = Modifier
                            .preferredSize(24.dp)
                    )
                }

                Text(
                    text = screenTitle,
                    color = colorResource(id = R.color.colorPrimary),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .constrainAs(title) {
                            centerTo(parent)
                        }
                )
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
