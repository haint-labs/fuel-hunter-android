package fuel.hunter.scenes.base

import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_BOTTOM
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_MIDDLE
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_TOP

typealias ViewTypeDetector = (index: Int, total: Int) -> Int

const val VIEW_TYPE_CATEGORY = -1

object ViewTypeDetectors {
    val Default: ViewTypeDetector = { index, total ->
        when (index) {
            0 -> SHADOW_TOP
            total -> SHADOW_BOTTOM
            else -> SHADOW_MIDDLE
        }
    }

    var Category: ViewTypeDetector = type@{ index: Int, total: Int ->
        if (index == 0) return@type -1
        if (index == 1) return@type SHADOW_TOP
        if (index == total) return@type SHADOW_BOTTOM
        SHADOW_MIDDLE
    }
}
