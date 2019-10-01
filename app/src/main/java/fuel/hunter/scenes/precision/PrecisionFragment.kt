package fuel.hunter.scenes.precision

import android.view.View
import fuel.hunter.R
import fuel.hunter.scenes.base.*
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_BOTTOM
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_MIDDLE
import fuel.hunter.view.shadow.ShadowView.Companion.SHADOW_TOP
import kotlinx.android.synthetic.main.layout_price_item.view.*

internal val precisionInfo = listOf(
    PrecisionTypedItem(
        ITEM_TYPE_TEXT,
        PrecisionInfo.Summary
    ),
    PrecisionTypedItem(
        SHADOW_TOP,
        PrecisionInfo.FuelProvider(
            R.drawable.logo_neste,
            "Neste",
            "Lētākā degviela Rīgā."
        )
    ),
    PrecisionTypedItem(
        SHADOW_MIDDLE,
        PrecisionInfo.FuelProvider(
            R.drawable.logo_circlek,
            "Circle K",
            "Lētākā degviela Rīgā."
        )
    ),
    PrecisionTypedItem(
        SHADOW_MIDDLE,
        PrecisionInfo.FuelProvider(
            R.drawable.logo_kool,
            "Kool",
            "Lētākā degviela Rīgā."
        )
    ),
    PrecisionTypedItem(
        SHADOW_MIDDLE,
        PrecisionInfo.FuelProvider(
            R.drawable.logo_ln,
            "Latvijas Nafta",
            "Zemākās cenas DUS tīklā Latvijā, pa reģioniem (Rīgas rajons, Liepājas rajons, Ventspils rajons, Zemgale, Vidzeme, Latgale)"
        )
    ),
    PrecisionTypedItem(
        SHADOW_MIDDLE,
        PrecisionInfo.FuelProvider(
            R.drawable.logo_viada,
            "Viada",
            "Patreiz nav pieejamas cenas."
        )
    ),
    PrecisionTypedItem(
        SHADOW_MIDDLE,
        PrecisionInfo.FuelProvider(
            R.drawable.logo_virshi,
            "Virši",
            "Lētākā degviela Rīgā un Pierīgā."
        )
    ),
    PrecisionTypedItem(
        SHADOW_MIDDLE,
        PrecisionInfo.FuelProvider(
            R.drawable.logo_gotika,
            "Gotika Auto",
            "Zemākās cenas DUS tīklā Latvijā."
        )
    ),
    PrecisionTypedItem(
        SHADOW_MIDDLE,
        PrecisionInfo.FuelProvider(
            R.drawable.logo_astarte,
            "ASTARTE",
            "Patreiz nav pieejamas cenas."
        )
    ),
    PrecisionTypedItem(
        SHADOW_BOTTOM,
        PrecisionInfo.FuelProvider(
            R.drawable.logo_dinaz,
            "DINAZ",
            "Patreiz nav pieejamas cenas."
        )
    )
)

internal class PrecisionFragment : BaseFragment<PrecisionTypedItem>() {
    override val title: Int get() = R.string.title_precision
    override val items = precisionInfo

    override var viewTypeDetector = ViewTypeDetectors.Category

    override val layoutProvider: ViewLayoutProvider = { viewType ->
        when (viewType) {
            VIEW_TYPE_CATEGORY -> R.layout.layout_precision_disclaimer
            else -> R.layout.layout_price_item
        }
    }

    override val binder: ViewHolderBinder<PrecisionTypedItem> = { view, typedItem ->
        when (typedItem.item) {
            is PrecisionInfo.FuelProvider -> {
                val item = typedItem.item

                view.apply {
                    icon.setImageResource(item.logo)
                    title.text = item.name
                    text.text = item.description
                    accent.visibility = View.GONE
                }
            }
        }
    }
}
