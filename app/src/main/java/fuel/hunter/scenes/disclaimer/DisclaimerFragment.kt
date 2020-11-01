package fuel.hunter.scenes.disclaimer

import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import fuel.hunter.R
import fuel.hunter.databinding.LayoutPriceItemBinding
import fuel.hunter.scenes.base.*

internal sealed class Disclaimer {
    object Summary : Disclaimer()

    class Provider(
        val logo: Int,
        val name: String,
        val description: String
    ) : Disclaimer()
}

internal class DisclaimerFragment : BaseFragment<Disclaimer>() {
    override val title = R.string.title_precision

    override val itemDiff = object: DiffUtil.ItemCallback<Disclaimer>() {
        override fun areItemsTheSame(oldItem: Disclaimer, newItem: Disclaimer) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Disclaimer, newItem: Disclaimer) = oldItem == newItem
    }

    override var viewTypeDetector = ViewTypeDetectors.Category

    override val layoutProvider: ViewLayoutProvider = {
        when (it) {
            VIEW_TYPE_CATEGORY -> R.layout.disclaimer_header
            else -> R.layout.layout_price_item
        }
    }

    override val binder: ViewHolderBinder<Disclaimer> = { view, item ->
        when (item) {
            is Disclaimer.Provider -> with(LayoutPriceItemBinding.bind(view)) {
                view.apply {
                    icon.setImageResource(item.logo)
                    title.text = item.name
                    text.text = item.description
                    accent.isGone = true
                }
            }
        }
    }
}

internal val precisionItems = listOf(
    Disclaimer.Summary,
    Disclaimer.Provider(
        R.drawable.logo_neste,
        "Neste",
        "Lētākā degviela Rīgā."
    ),
    Disclaimer.Provider(
        R.drawable.logo_circlek,
        "Circle K",
        "Lētākā degviela Rīgā."
    ),
    Disclaimer.Provider(
        R.drawable.logo_kool,
        "Kool",
        "Lētākā degviela Rīgā."
    ),
    Disclaimer.Provider(
        R.drawable.logo_ln,
        "Latvijas Nafta",
        "Zemākās cenas DUS tīklā Latvijā, pa reģioniem (Rīgas rajons, Liepājas rajons, Ventspils rajons, Zemgale, Vidzeme, Latgale)"
    ),
    Disclaimer.Provider(
        R.drawable.logo_viada,
        "Viada",
        "Patreiz nav pieejamas cenas."
    ),
    Disclaimer.Provider(
        R.drawable.logo_virshi,
        "Virši",
        "Lētākā degviela Rīgā un Pierīgā."
    ),
    Disclaimer.Provider(
        R.drawable.logo_gotika,
        "Gotika Auto",
        "Zemākās cenas DUS tīklā Latvijā."
    ),
    Disclaimer.Provider(
        R.drawable.logo_astarte,
        "ASTARTE",
        "Patreiz nav pieejamas cenas."
    ),
    Disclaimer.Provider(
        R.drawable.logo_dinaz,
        "DINAZ",
        "Patreiz nav pieejamas cenas."
    )
)