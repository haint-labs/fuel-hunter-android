package fuel.hunter.scenes.settings

import androidx.annotation.DrawableRes

sealed class Fuel {
    object Header : Fuel()

    data class Cheapest(val isChecked: Boolean) : Fuel()

    sealed class Logo {
        class Drawable(@DrawableRes val id: Int): Logo()
        class Url(val url: String): Logo()
    }

    data class Company(
        val logo: Logo,
        val name: String,
        val isChecked: Boolean
    ) : Fuel()

    data class Type(
        val name: String,
        val isChecked: Boolean = false
    ) : Fuel()
}