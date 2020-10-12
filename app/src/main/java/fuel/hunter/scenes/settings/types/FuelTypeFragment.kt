package fuel.hunter.scenes.settings.types

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import fuel.hunter.R
import fuel.hunter.extensions.onChecked
import fuel.hunter.scenes.base.BaseFragment
import fuel.hunter.scenes.base.VIEW_TYPE_CATEGORY
import fuel.hunter.scenes.base.ViewLayoutProvider
import fuel.hunter.scenes.base.ViewTypeDetectors
import fuel.hunter.scenes.settings.Fuel
import kotlinx.android.synthetic.main.layout_setting_item.view.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class FuelTypeFragment : BaseFragment<Fuel>() {
    private val viewModel by viewModels<FuelTypeViewModel>()

    override val title = R.string.title_fuel_type

    override val itemDiff = object : DiffUtil.ItemCallback<Fuel>() {
        override fun areItemsTheSame(oldItem: Fuel, newItem: Fuel) = when {
            oldItem is Fuel.Header && newItem is Fuel.Header -> true
            oldItem is Fuel.Type && newItem is Fuel.Type -> oldItem.name == newItem.name
            else -> false
        }

        override fun areContentsTheSame(oldItem: Fuel, newItem: Fuel) = when {
            oldItem is Fuel.Header && newItem is Fuel.Header -> true
            oldItem is Fuel.Type && newItem is Fuel.Type -> oldItem == newItem
            else -> false
        }
    }

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
                    settingsDescription.isGone = true

                    with(settingToggle) {
                        isChecked = item.isChecked

                        onChecked
                            .map { item.copy(isChecked = it) }
                            .onEach { viewModel.updateFuelTypePreference(it) }
                            .launchIn(lifecycleScope)
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fuelTypeUiMap
            .onEach { adapter.submitList(it) }
            .launchIn(lifecycleScope)
    }
}