package fuel.hunter.scenes.base.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

interface ListItemType {
    val separator: @Composable () -> Unit
    val padding: PaddingValues
    val glow: (size: Size, offset: Float, cornerRadius: Float) -> Path
}

object Single : ListItemType {
    override val separator: @Composable () -> Unit = { Separator() }

    override val padding = PaddingValues(
        top = 6.dp,
        bottom = 6.dp,
        start = 10.dp,
        end = 10.dp,
    )

    override val glow = { size: Size, offset: Float, cornerRadius: Float ->
        Path().apply {
            addRoundRect(
                RoundRect(
                    left = offset,
                    top = offset,
                    right = size.width - offset,
                    bottom = size.height - offset,
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                )
            )

            addRect(
                Rect(
                    left = offset,
                    top = offset,
                    right = size.width - 2 * offset,
                    bottom = size.height - 2 * offset,
                )
            )
        }
    }
}

object Top : ListItemType {
    override val separator: @Composable () -> Unit = { Separator() }

    override val padding = PaddingValues(
        top = 10.dp,
        bottom = 6.dp,
        start = 10.dp,
        end = 10.dp,
    )

    override val glow = { size: Size, offset: Float, cornerRadius: Float ->
        Path().apply {
            addRoundRect(
                RoundRect(
                    left = offset,
                    top = offset,
                    right = size.width - offset,
                    bottom = size.height,
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                )
            )

            addRect(
                Rect(
                    left = offset,
                    top = offset,
                    right = size.width - 2 * offset,
                    bottom = size.height,
                )
            )

            addRect(
                Rect(
                    left = offset,
                    top = 2 * offset,
                    right = size.width - offset,
                    bottom = size.height,
                )
            )
        }
    }
}

object Middle : ListItemType {
    override val separator: @Composable () -> Unit = { Separator() }

    override val padding = PaddingValues(
        top = 6.dp,
        bottom = 6.dp,
        start = 10.dp,
        end = 10.dp,
    )

    override val glow = { size: Size, offset: Float, _: Float ->
        Path().apply {
            addRect(
                Rect(
                    left = offset,
                    top = 0f,
                    right = size.width - offset,
                    bottom = size.height,
                )
            )
        }
    }
}

object Bottom : ListItemType {
    override val separator: @Composable () -> Unit = {}

    override val padding = PaddingValues(
        top = 6.dp,
        bottom = 8.dp,
        start = 10.dp,
        end = 10.dp,
    )

    override val glow = { size: Size, offset: Float, cornerRadius: Float ->
        Path().apply {
            addRoundRect(
                RoundRect(
                    left = offset,
                    top = offset,
                    right = size.width - offset,
                    bottom = size.height - offset,
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                )
            )

            addRect(
                Rect(
                    left = offset,
                    top = 0f,
                    right = size.width - offset,
                    bottom = size.height - 2 * offset,
                )
            )
        }
    }
}