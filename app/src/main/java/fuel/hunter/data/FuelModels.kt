package fuel.hunter.data

import androidx.annotation.DrawableRes
import fuel.hunter.R

sealed class Fuel {
    data class Category(val name: String) : Fuel()

    sealed class Logo {
        class Drawable(@DrawableRes val id: Int): Logo()
        class Url(val url: String): Logo()
    }

    data class Price(
        val title: String,
        val address: String,
        val type: String,
        val price: Float,
        val logo: Logo
    ) : Fuel()
}

internal val dummyData = mapOf(
    Fuel.Category("95 | Benzīns") to listOf(
        Fuel.Price(
            "Kool",
            "Rīga - Senču iela 2b",
            "E95",
            1.125f,
            Fuel.Logo.Drawable(R.drawable.logo_kool),
        ),
        Fuel.Price(
            "Kool",
            "Rīga - Senču iela 2b",
            "E95",
            1.125f,
            Fuel.Logo.Drawable(R.drawable.logo_kool),
        ),
        Fuel.Price(
            "Viada",
            "Rīga - Senču iela 2b, Brīvības iela 82a",
            "E95",
            1.125f,
            Fuel.Logo.Drawable(R.drawable.logo_viada),
        ),
        Fuel.Price(
            "Dinaz",
            "Rīga - Senču iela 2b, Brīvības iela 82a",
            "E95",
            1.125f,
            Fuel.Logo.Drawable(R.drawable.logo_dinaz),
        )
    ),
    Fuel.Category("98 | Benzīns") to listOf(
        Fuel.Price(
            "Kool",
            "Rīga - Senču iela 2b",
            "E98",
            1.125f,
            Fuel.Logo.Drawable(R.drawable.logo_kool),
        )
    ),
    Fuel.Category("DD | Dīzeļdegviela") to listOf(
        Fuel.Price(
            "Virši",
            "Rīga - Senču iela 2b, Katoļu 4, Kurzemes prospekts 4, Lugažu 6, Brīvības iela 82a",
            "E98",
            1.125f,
            Fuel.Logo.Drawable(R.drawable.logo_virshi),
        ),
        Fuel.Price(
            "AStarte",
            "Rīga - Jūrkalnes iela 6, Lugažu 6, Brīvības iela 82a",
            "E98",
            1.012f,
            Fuel.Logo.Drawable(R.drawable.logo_astarte),
        ),
        Fuel.Price(
            "Latvijas nafta",
            "Rīga - Senču iela 2b, Katoļu 4, Kurzemes prospekts 4, Lugažu 6, Brīvības iela 82a",
            "E98",
            2.301f,
            Fuel.Logo.Drawable(R.drawable.logo_ln),
        )
    ),
    Fuel.Category("98 | Benzīns") to listOf(
        Fuel.Price(
            "Circle K",
            "Rīga - Jūrkalnes iela 6, Lugažu 6, Brīvības iela 82a",
            "E98",
            2.301f,
            Fuel.Logo.Drawable(R.drawable.logo_circlek),
        )
    )
)