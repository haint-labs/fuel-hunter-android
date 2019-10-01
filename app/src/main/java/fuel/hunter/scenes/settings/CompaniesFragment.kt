package fuel.hunter.scenes.settings

import android.view.View
import android.widget.TextView
import androidx.core.view.isGone
import fuel.hunter.R
import fuel.hunter.scenes.base.BaseFragment
import fuel.hunter.scenes.base.VIEW_TYPE_CATEGORY
import fuel.hunter.scenes.base.ViewLayoutProvider
import fuel.hunter.scenes.base.ViewTypeDetectors
import kotlinx.android.synthetic.main.layout_setting_item.view.*

sealed class Fuel {
    object Description : Fuel()

    data class Cheapest(val isChecked: Boolean) : Fuel()

    data class Company(
        val logo: Int,
        val name: String,
        val isChecked: Boolean
    ) : Fuel()
}

class CompaniesFragment : BaseFragment<Fuel>() {
    override val title = R.string.title_companies
    override val items = fuelCompanies

    override var viewTypeDetector = ViewTypeDetectors.Category

    override val layoutProvider: ViewLayoutProvider = {
        when (it) {
            VIEW_TYPE_CATEGORY -> R.layout.layout_setting_header
            else -> R.layout.layout_setting_item
        }
    }

    override val binder = { view: View, item: Fuel ->
        with(view) {
            when (item) {
                is Fuel.Description -> {
                    if (view !is TextView) {
                        return@with
                    }

                    view.text = "Atzīmē kuras uzpildes kompānijas\nvēlies redzēt sarakstā."
                }
                is Fuel.Cheapest -> {
                    settingTitle.text = "Lētākā"
                    settingsDescription.text =
                        "Ieslēdzot šo - vienmēr tiks rādīta arī tā kompānija, kurai Latvijā ir lētākā degviela attiecīgajā brīdī"
                    settingToggle.isChecked = item.isChecked
                }
                is Fuel.Company -> {
                    settingTitle.text = item.name
                    settingToggle.isChecked = item.isChecked
                    settingsDescription.isGone = true
                    settingsIcon.isGone = false
                    settingsIcon.setImageDrawable(resources.getDrawable(item.logo, context.theme))
                }
            }
        }
    }
}

private val fuelCompanies = listOf(
    Fuel.Description,
    Fuel.Cheapest(false),
    Fuel.Company(R.drawable.logo_neste, "NESTE", true),
    Fuel.Company(R.drawable.logo_circlek, "Circle K", true),
    Fuel.Company(R.drawable.logo_kool, "Kool", false),
    Fuel.Company(R.drawable.logo_ln, "Latvijas Nafta", false),
    Fuel.Company(R.drawable.logo_viada, "Viada", false),
    Fuel.Company(R.drawable.logo_virshi, "Virši", false),
    Fuel.Company(R.drawable.logo_gotika, "Gotika Auto", false),
    Fuel.Company(R.drawable.logo_astarte, "ASTARTE", false),
    Fuel.Company(R.drawable.logo_dinaz, "DINAZ", false)
)