package fuel.hunter.scenes.precision

import fuel.hunter.extensions.TypedItem

internal const val ITEM_TYPE_TEXT = -1

internal sealed class PrecisionInfo {
    object Summary : PrecisionInfo()

    class FuelProvider(
        val logo: Int,
        val name: String,
        val description: String
    ) : PrecisionInfo()
}

internal typealias PrecisionTypedItem = TypedItem<Int, PrecisionInfo>
