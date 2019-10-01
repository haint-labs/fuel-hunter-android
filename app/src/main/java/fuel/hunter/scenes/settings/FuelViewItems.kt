package fuel.hunter.scenes.settings

sealed class Fuel {
    object Header : Fuel()

    data class Cheapest(val isChecked: Boolean) : Fuel()

    data class Company(
        val logo: Int,
        val name: String,
        val isChecked: Boolean
    ) : Fuel()

    data class Type(
        val name: String,
        val isChecked: Boolean = false
    ) : Fuel()
}