package fuel.hunter.scenes.prices

import fuel.hunter.data.Fuel
import fuel.hunter.extensions.TypedItem
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_BOTTOM
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_MIDDLE
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_SINGLE
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_TOP

internal typealias FuelTypedItem = TypedItem<Int, Fuel>

internal const val ITEM_TYPE_CATEGORY = -1

fun flattenFuelTypes(
    data: Map<Fuel.Category, List<Fuel.Price>>
): List<FuelTypedItem> {
    return data.entries.flatMap { (category, prices) ->
        val result = mutableListOf(
            FuelTypedItem(ITEM_TYPE_CATEGORY, category)
        )

        if (prices.size == 1) {
            return@flatMap result + FuelTypedItem(SHADOW_SINGLE, prices.first())
        }

        return@flatMap result + prices.mapIndexed { index, item ->
            val type = when (index) {
                0 -> SHADOW_TOP
                prices.lastIndex -> SHADOW_BOTTOM
                else -> SHADOW_MIDDLE
            }

            FuelTypedItem(type, item)
        }
    }
}