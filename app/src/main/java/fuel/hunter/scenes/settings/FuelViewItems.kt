package fuel.hunter.scenes.settings

import androidx.annotation.DrawableRes

sealed class Fuel {
    sealed class Logo {
        class Drawable(@DrawableRes val id: Int): Logo()
        class Url(val url: String): Logo()
    }

    data class Company(
        val name: String,
        val isChecked: Boolean,
        val logo: Logo? = null,
        val description: String? = null,
    ) : Fuel()

    data class Type(
        val name: String,
        val isChecked: Boolean = false
    ) : Fuel()
}