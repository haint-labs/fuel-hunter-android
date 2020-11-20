package fuel.hunter.view.decorations

import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.Interaction
import androidx.compose.foundation.InteractionState
import androidx.compose.ui.ContentDrawScope
import androidx.compose.ui.graphics.Color

fun roundIndication(color: Color): Indication {
    return object : Indication {
        override fun createInstance(): IndicationInstance {
            return RoundIndicationInstance(color = color)
        }
    }
}

internal class RoundIndicationInstance(val color: Color) : IndicationInstance {
    override fun ContentDrawScope.drawIndication(interactionState: InteractionState) {
        drawContent()
        if (interactionState.contains(Interaction.Pressed)) {
            drawCircle(color = color)
        }
    }
}