package fuel.hunter.scenes.settings.companies

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import coil.load
import fuel.hunter.R
import fuel.hunter.databinding.LayoutSettingHeaderBinding
import fuel.hunter.databinding.LayoutSettingItemBinding
import fuel.hunter.extensions.onChecked
import fuel.hunter.scenes.base.BaseFragment
import fuel.hunter.scenes.base.VIEW_TYPE_CATEGORY
import fuel.hunter.scenes.base.ViewLayoutProvider
import fuel.hunter.scenes.base.ViewTypeDetectors
import fuel.hunter.scenes.settings.Fuel
import fuel.hunter.tools.di.viewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class CompaniesFragment : BaseFragment<Fuel>() {
    private val viewModel by viewModel<CompaniesViewModel>()

    override val title = R.string.title_companies

    override val itemDiff = object : DiffUtil.ItemCallback<Fuel>() {
        override fun areItemsTheSame(oldItem: Fuel, newItem: Fuel) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Fuel, newItem: Fuel) = oldItem == newItem
    }

    override var viewTypeDetector = ViewTypeDetectors.Category

    override val layoutProvider: ViewLayoutProvider = {
        when (it) {
            VIEW_TYPE_CATEGORY -> R.layout.layout_setting_header
            else -> R.layout.layout_setting_item
        }
    }

    override val binder = { view: View, item: Fuel ->
        when (item) {
            is Fuel.Header -> with(LayoutSettingHeaderBinding.bind(view)) {
                settingHeader.text = "Atzīmē kuras uzpildes kompānijas\nvēlies redzēt sarakstā."
            }
            is Fuel.Cheapest -> with(LayoutSettingItemBinding.bind(view)) {
                settingTitle.text = "Lētākā"
                settingsDescription.text =
                    "Ieslēdzot šo - vienmēr tiks rādīta arī tā kompānija, kurai Latvijā ir lētākā degviela attiecīgajā brīdī"
                settingToggle.isChecked = item.isChecked
            }
            is Fuel.Company -> with(LayoutSettingItemBinding.bind(view)) {
                settingTitle.text = item.name
                settingsDescription.isGone = true

                with(settingsIcon) {
                    isGone = false

                    when (val it = item.logo) {
                        is Fuel.Logo.Url -> load(it.url)
                        is Fuel.Logo.Drawable -> setImageDrawable(
                            resources.getDrawable(it.id, context.theme)
                        )
                    }
                }

                with(settingToggle) {
                    isChecked = item.isChecked

                    onChecked
                        .map { item.copy(isChecked = it) }
                        .onEach { viewModel.updateCompaniesPreference(it) }
                        .launchIn(lifecycleScope)
                }
            }
        }
        Unit
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.companiesUiMap
            .onEach { adapter.submitList(it) }
            .launchIn(lifecycleScope)
    }
}