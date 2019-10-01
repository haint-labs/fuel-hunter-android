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

class FuelTypeFragment : BaseFragment<Fuel>() {
    override val title = R.string.title_fuel_type
    override val items = fuelItems

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
                is Fuel.Header -> {
                    if (view !is TextView) {
                        return@with
                    }

                    view.text = "Atzīmē degvielas veidus, kuru cenas\nTev interesē."
                }
                is Fuel.Type -> {
                    settingTitle.text = item.name
                    settingToggle.isChecked = item.isChecked
                    settingsDescription.isGone = true
                }
            }
        }
    }
}

private val fuelItems = listOf(
    Fuel.Header,
    Fuel.Type("DD | Dīzeļdegviela"),
    Fuel.Type("DD | Pro Dīzeļdegviela"),
    Fuel.Type("95 | Benzīns"),
    Fuel.Type("98 | Benzīns"),
    Fuel.Type("Auto gāze")
)