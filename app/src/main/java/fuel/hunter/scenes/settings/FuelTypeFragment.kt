package fuel.hunter.scenes.settings

import android.view.View
import android.widget.TextView
import androidx.core.view.isGone
import fuel.hunter.R
import fuel.hunter.scenes.base.BaseFragment
import fuel.hunter.scenes.base.ViewTypeDetector
import fuel.hunter.scenes.base.ViewTypeDetectors
import kotlinx.android.synthetic.main.layout_setting_item.view.*

sealed class FuelTypes {
    object Header : FuelTypes()
    data class FuelType(val name: String, val isChecked: Boolean = false) : FuelTypes()
}

private val fuelTypes = listOf(
    FuelTypes.Header,
    FuelTypes.FuelType("DD | Dīzeļdegviela"),
    FuelTypes.FuelType("DD | Pro Dīzeļdegviela"),
    FuelTypes.FuelType("95 | Benzīns"),
    FuelTypes.FuelType("98 | Benzīns"),
    FuelTypes.FuelType("Auto gāze")
)

class FuelTypeFragment : BaseFragment<FuelTypes>() {
    override val title = R.string.title_fuel_type
    override val items = fuelTypes

    override var viewTypeDetector: ViewTypeDetector = ViewTypeDetectors.Category

    override val layoutProvider = { viewType: Int ->
        when (viewType) {
            -1 -> R.layout.layout_setting_header
            else -> R.layout.layout_setting_item
        }
    }

    override val binder = { view: View, item: FuelTypes ->
        with(view) {
            when (item) {
                is FuelTypes.Header -> {
                    if (view !is TextView) {
                        return@with
                    }

                    view.text = "Atzīmē degvielas veidus, kuru cenas\nTev interesē."
                }
                is FuelTypes.FuelType -> {
                    settingTitle.text = item.name
                    settingToggle.isChecked = item.isChecked
                    settingsDescription.isGone = true
                }
            }
        }
    }
}