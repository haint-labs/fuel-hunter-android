package fuel.hunter.tools

fun <K, V> mapper(vararg pairs: Pair<K, V>): Mapper<K, V> {
    val map = pairs.associateBy(
        keySelector = { it.first },
        valueTransform = { it.second }
    )

    return object : Mapper<K, V> {
        override val mapping: Map<K, V> get() = map
    }
}

interface Mapper<K, V> {
    val mapping: Map<K, V>

    fun valueFor(key: K): V {
        return mapping[key] ?: throw IllegalArgumentException("No value for key - $key")
    }

    fun keyFor(value: V): K {
        return mapping.entries.first { it.value == value }.key
    }
}