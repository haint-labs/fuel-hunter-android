package fuel.hunter.view.decorations

import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope

fun roundIndication(color: Color): Indication {
    return object : Indication {
        @Composable
        override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
            val isPressed = interactionSource.collectIsPressedAsState()
            return remember(interactionSource) {
                RoundIndication(isPressed = isPressed, color = color)
            }
        }
    }
}

class RoundIndication(
    private val isPressed: State<Boolean>,
    private val color: Color,
) : IndicationInstance {
    override fun ContentDrawScope.drawIndication() {
        drawContent()
        if (isPressed.value) {
            drawCircle(color = color)
        }
    }
}