package fuel.hunter.data

import fuel.hunter.R
import fuel.hunter.extensions.*

sealed class Item(var typeId: Int)

data class FuelPrice(
    val title: String,
    val address: String,
    val price: Float,
    val logo: Int
) : Item(1)

data class FuelCategory(
    val name: String
) : Item(2)

internal val dummyData = mapOf(
    FuelCategory("DD | Dīzeļdegviela") to listOf(
        FuelPrice(
            "Neste",
            "Rīga - Senču iela 2b, Katoļu 4, Kurzemes prospekts 4, Lugažu 6, Brīvības iela 82a",
            1.125f,
            R.drawable.logo_neste
        ),
        FuelPrice(
            "AStarte",
            "Rīga - Jūrkalnes iela 6",
            1.012f,
            R.drawable.logo_astarte
        ),
        FuelPrice(
            "Circle K",
            "Rīga - Jūrkalnes iela 6, Lugažu 6, Brīvības iela 82a",
            2.301f,
            R.drawable.logo_circlek
        )
    ),
    FuelCategory("98 | Benzīns") to listOf(
        FuelPrice(
            "Circle K",
            "Rīga - Jūrkalnes iela 6, Lugažu 6, Brīvības iela 82a",
            2.301f,
            R.drawable.logo_circlek
        )
    )
).flatMap {
    val cat = it.key
    cat.typeId = ITEM_TYPE_CATEGORY
    listOf(cat)

    if (it.value.size == 1) {
        val item = it.value.first()
        item.typeId = ITEM_TYPE_SINGLE
        return@flatMap listOf(cat, item)
    }

    return@flatMap listOf(cat) + it.value.mapIndexed { index, item ->
        if (index == 0) {
            item.typeId = ITEM_TYPE_HEADER
            return@mapIndexed item
        }

        if (index == it.value.size - 1) {
            item.typeId = ITEM_TYPE_FOOTER
            return@mapIndexed item
        }

        item.typeId = ITEM_TYPE_MIDDLE
        return@mapIndexed item
    }
}