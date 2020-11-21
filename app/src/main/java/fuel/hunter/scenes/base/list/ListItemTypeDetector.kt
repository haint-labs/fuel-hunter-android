package fuel.hunter.scenes.base.list

interface ListItemTypeDetector {
    fun getType(index: Int): ListItemType
}

class IndexListItemTypeDetector(
    private val totalAmount: Int,
) : ListItemTypeDetector {
    override fun getType(index: Int) = when {
        totalAmount == 1 -> Single
        index == 0 -> Top
        index == totalAmount - 1 -> Bottom
        else -> Middle
    }
}