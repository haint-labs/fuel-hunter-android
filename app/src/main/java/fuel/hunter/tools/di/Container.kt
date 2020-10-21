package fuel.hunter.tools.di

fun container(init: Container.() -> Unit): Lazy<Container> = lazy { Container().also(init) }

interface ContainerHolder {
    val container: Container
}

sealed class Dependency {
    class Item<T>(val item: T) : Dependency()
    class Factory<T>(val factory: Container.() -> T) : Dependency()
}

class Container {
    @PublishedApi
    internal val items = mutableMapOf<String, Dependency>()

    inline fun <reified T> single(
        value: T,
        key: String = T::class.java.simpleName
    ) {
        items[key] = Dependency.Item(value)
    }

    inline fun <reified T> factory(
        key: String = T::class.java.simpleName,
        noinline factory: Container.() -> T,
    ) {
        items[key] = Dependency.Factory(factory)
    }

    fun <T> provide(key: String, clazz: Class<T>): T {
        val value = when (val raw = this.items[key]) {
            is Dependency.Item<*> -> raw.item
            is Dependency.Factory<*> -> raw.factory(this)
            else -> throw IllegalStateException()
        }

        @Suppress("UNCHECKED_CAST")
        return value as? T ?: throw TypeCastException(
            "Unable to cast - key: $key, class: ${clazz.simpleName}"
        )
    }

    inline fun <reified T> get(key: String = T::class.java.simpleName): T =
        provide(key, T::class.java)
}